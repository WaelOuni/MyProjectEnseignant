package weal.exemple.com.myprojectenseignant;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

/**
 * Created by WIN on 31/05/2015.
 */
public class StatT  extends Activity {
    Intent achartIntent= null;
    List<NameValuePair> params = new ArrayList<NameValuePair>();
    static int newInt , zerocinq=0, cinqdix=0, dixquinze=0, quinzevingt=0;
    static String[] strs;
    static Integer[] reps;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistique_test);

        Bundle  extras = getIntent().getExtras();
        if(extras == null) {
        } else {

            String source=getCallingActivity().getShortClassName();
            source=source.substring(1);
            newInt=  extras.getInt("idtest");
            //  		newInt= Statistique.id;
            //	Toast.makeText(getApplicationContext(), String.valueOf(newInt), Toast.LENGTH_LONG).show();
            params.add(new BasicNameValuePair("id", String.valueOf(newInt)));
            Toast.makeText(getApplicationContext(), String.valueOf(newInt), Toast.LENGTH_LONG).show();
            DownloadTask dlTask = new DownloadTask();
            dlTask.execute();
            findViewById(R.id.affichStatBtn).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
                    Toast.makeText(getApplicationContext(), String.valueOf(zerocinq), Toast.LENGTH_LONG).show();
                    Toast.makeText(getApplicationContext(), String.valueOf(cinqdix), Toast.LENGTH_LONG).show();
                    Toast.makeText(getApplicationContext(), String.valueOf(dixquinze), Toast.LENGTH_LONG).show();
                    Toast.makeText(getApplicationContext(), String.valueOf(quinzevingt), Toast.LENGTH_LONG).show();
                    MyCharteTestStat m1= new MyCharteTestStat();
                    achartIntent = m1.createIntent(StatT.this);
                    startActivity(achartIntent);
                    finish();

                }
            });
        }
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment()).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.statistique_test, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(
                    R.layout.fragment_statistique_test, container, false);
            return rootView;
        }
    }

    public void setUrl(String newstr){
        strURL+=newstr;
    }

    public   String strURL = "http://10.0.3.2/MyProjectConnect/Enseignant/";
    public class DownloadTask extends AsyncTask<UrlEncodedFormEntity, Void, String> {

        @Override
        protected String doInBackground(UrlEncodedFormEntity... urls) {
            // TODO Auto-generated method stub
            String returnString = "";
            InputStream is = null;
            String result = "";
            // Envoie de la commande http
            try{
                HttpClient httpclient = new DefaultHttpClient();
                setUrl("resultatsStatistique.php");
                HttpPost httppost = new HttpPost(strURL);
                httppost.setEntity(new UrlEncodedFormEntity(params));
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                is = entity.getContent();
            }
            catch(Exception e){
                Log.e("log_tag", "Error in http connection " + e.toString());
            }
            // Convertion de la requÃªte en string
            try{
                BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                is.close();
                result=sb.toString();
            }
            catch(Exception e){
                Log.e("log_tag", "Error converting result to string " + e.toString());
            }
            //Parse les donnÃ©es JSON°
            try{
                JSONArray jArray = new JSONArray(result);
                for(int i=0;i<jArray.length();i++){
                    JSONObject json_data = jArray.getJSONObject(i);
                    // Affichage ID_question et Nom_question dans le LogCat
                    Log.i("log_tag"," numrepjust: "+json_data.getString("numrepjust")
                    );
                    // RÃ©sultats de la requÃªte

                    returnString += "\n\t"+ json_data.getInt("numrepjust");
                    if(json_data.getInt("numrepjust")>=0){
                        if(json_data.getInt("numrepjust")<=1){zerocinq++;}
                        else if(json_data.getInt("numrepjust")==2){cinqdix++;}
                        else if(json_data.getInt("numrepjust")==3){dixquinze++;}
                        else if(json_data.getInt("numrepjust")>=4){quinzevingt++;}
                    }
                }
            }
            catch(JSONException e){
                Log.e("log_tag", "Error parsing data " + e.toString());
            }
            return returnString;
        }

        protected void onPostExecute(String returnString) {
            //	Toast.makeText(AllQuestCC.this,returnString, Toast.LENGTH_LONG).show();
            strs=returnString.split("\n\t");
            //	reps= new Integer[strs.length-1];
        }
    }

}

