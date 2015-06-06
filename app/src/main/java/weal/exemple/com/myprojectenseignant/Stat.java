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
import android.widget.*;

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
import java.util.Calendar;
import java.util.List;

/**
 * Created by WIN on 31/05/2015.
 */
public class Stat extends Activity {
    Button btn, conf;
    EditText mat, sea;
    DatePicker dat;
    int id, jour, mois, annee;
    String matstatStr, datestatStr, seastatStr;
    static Intent choixIntent;
    List<NameValuePair> params = new ArrayList<NameValuePair>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistique);

        mat= (EditText) findViewById(R.id.matStatTestInp);
        sea=(EditText) findViewById(R.id.seanStatTestInp);
        dat= (DatePicker) findViewById(R.id.dateStat);
        jour=dat.getDayOfMonth();
        mois= dat.getMonth()+1;
        annee= dat.getYear();
        final Calendar calendar = Calendar.getInstance();
        annee = calendar.get(Calendar.YEAR);
        mois = calendar.get(Calendar.MONTH);
        jour = calendar.get(Calendar.DAY_OF_MONTH);
        dat.init(annee, mois, jour, null);
        btn =(Button) findViewById(R.id.VoirStatBtn);
        btn.setVisibility(View.INVISIBLE);
        conf =(Button) findViewById(R.id.confirmerStatBtn);
        conf.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                jour=dat.getDayOfMonth();
                mois= dat.getMonth()+1;
                annee= dat.getYear();
                datestatStr= jour+"-"+mois+"-"+annee;

                matstatStr = mat.getText().toString();
                seastatStr = sea.getText().toString();
                if ((matstatStr != null) && (matstatStr.trim().length() > 0) &&
                        (seastatStr != null) && (seastatStr.trim().length() > 0) ){
                    params.add(new BasicNameValuePair("seance", sea.getText().toString()));
                    params.add(new BasicNameValuePair("date", datestatStr));
                    params.add(new BasicNameValuePair("matiere", mat.getText().toString()));
                    DownloadTask dlTask = new DownloadTask();
                    dlTask.execute();
                    conf.setVisibility(View.INVISIBLE);
                }
                else Toast.makeText(getApplicationContext(), "Tous les champs sont obligatoires !!! !", Toast.LENGTH_LONG).show();

            }
        });
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                choixIntent = new Intent(getApplicationContext(), StatT.class);
                choixIntent.putExtra("idtest",id );
                startActivityForResult(choixIntent,80);
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
        getMenuInflater().inflate(R.menu.statistique, menu);
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
            View rootView = inflater.inflate(R.layout.fragment_statistique,
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

            String returnString = "";
            InputStream is = null;
            String result = "";

            // Envoie de la commande http
            try{
                HttpClient httpclient = new DefaultHttpClient();
                setUrl("resultattest.php");
                HttpPost httppost = new HttpPost(strURL);
                httppost.setEntity(new UrlEncodedFormEntity(params));
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                is = entity.getContent();
            }
            catch(Exception e){
                Log.e("log_tag", "Error in http connection " + e.toString());
            }
            // Convertion de la requête en string
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
            //Parse les données JSON
            try{
                JSONArray jArray = new JSONArray(result);
                for(int i=0;i<jArray.length();i++){
                    JSONObject json_data = jArray.getJSONObject(i);
                    // Affichage ID_question et Nom_question dans le LogCat
                    Log.i("log_tag","id: "+json_data.getInt("id")
                    );
                    id=json_data.getInt("id");
                    // Résultats de la requête
                    returnString +="\n\t"+ json_data.getInt("id");
                }
            }

            catch(JSONException e){
                Log.e("log_tag", "Error parsing data " + e.toString());
            }
            return returnString;
        }

        protected void onPostExecute(String returnString) {
            returnString=String.valueOf(id);

            btn.setVisibility(View.VISIBLE);
        }
    }

}

