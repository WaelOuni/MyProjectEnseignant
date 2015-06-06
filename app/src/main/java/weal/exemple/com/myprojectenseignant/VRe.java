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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by WIN on 31/05/2015.
 */
public class VRe extends Activity {
    ListView lv;
    ArrayAdapter<String> adap;
    Button autreResult, quit;
    static String[] strs;
    //Building Parameters
    List<NameValuePair> params = new ArrayList<NameValuePair>();
    static int newInt ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voir_resultat);

        Bundle  extras = getIntent().getExtras();
        if(extras == null) {
        } else {

            String source=getCallingActivity().getShortClassName();
            source=source.substring(1);

            newInt=  extras.getInt("identifiant");

            if (source.equals("ResultatTest")){

                params.add(new BasicNameValuePair("id", String.valueOf(newInt)));
                Toast.makeText(getApplicationContext(), String.valueOf(newInt), Toast.LENGTH_LONG).show();
            }

        }



        lv = (ListView) findViewById(R.id.listResultsLv);
        //Appeler la mÃ©thode pour rÃ©cupÃ©rer les donnÃ©es JSON
        DownloadTask dlTask = new DownloadTask();
        dlTask.execute();
        quit = (Button) findViewById(R.id.quitBtn);
        quit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                finish();
            }
        });
        autreResult = (Button) findViewById(R.id.autreresultBtn);
        autreResult.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                startActivity(new Intent(getApplicationContext(), ReT.class));
                finish();
            }
        });

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment()).commit();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.voir_resultat, menu);
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
            View rootView = inflater.inflate(R.layout.fragment_voir_resultat,
                    container, false);
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
                setUrl("resultatetudiants.php");
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
                    Log.i("log_tag","cin: "+json_data.getInt("cin") +
                                    ", nom: "+json_data.getString("nom") +
                                    ", prenom: "+json_data.getString("prenom") +
                                    ", numrepjust: "+json_data.getString("numrepjust")+
                                    ", mention: "+json_data.getString("mention") +
                                    ", rapidite: "+json_data.getString("rapidite")
                    );
                    // RÃ©sultats de la requÃªte
                    returnString += "\n\t ID: "+json_data.getInt("cin")+" NOM: "+ json_data.getString("nom")
                            +" PRENOM: "+ json_data.getString("prenom")+" N° REPONSE(s) JUSTE(s): "+ json_data.getString("numrepjust")
                            +" MENTION: "+ json_data.getString("mention")+" RAPIDITE: "+ json_data.getString("rapidite");
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
            adap = new ArrayAdapter<String>(VRe.this,

                    android.R.layout.simple_list_item_1, android.R.id.text1, strs);

            lv.setAdapter(adap);

        }
    }

}

