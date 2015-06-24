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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

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
public class EEn extends Activity {
    ListView lv;
    Button quitter;
    TextView bienvenue;
    static String[] strs;
    //Building Parameters
    List<NameValuePair> params = new ArrayList<NameValuePair>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_espace_enseignant);
        bienvenue= (TextView) findViewById(R.id.bienvenueString);
        String newString;
        Bundle  extras = getIntent().getExtras();
        if(extras == null) {
            newString= null;
        } else {

            String source=getCallingActivity().getShortClassName();
            source=source.substring(1);
            newString= (String) extras.getString("login");

            params.add(new BasicNameValuePair("email", newString));


            DownloadTask dlTask = new DownloadTask();
            dlTask.execute();
        }

        quitter = (Button) findViewById(R.id.quitterespaceBtn);
        quitter.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                startActivity(new Intent(EEn.this, LogA.class));
                finish();
            }
        });
        lv = (ListView)this.findViewById(R.id.menuenseignantLv);

        String[] values = new String[] { "Create question","Questions's list","Create test","all tests"
                ,"Tests's results","My students","My profil","Statistics "};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, values);

        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {
                // TODO Auto-generated method stub
                int itemPosition     = position;

                // ListView Clicked item value
                String  itemValue    = (String) lv.getItemAtPosition(position);


                switch (position) {

                    case 0:
                        startActivity(new Intent(getApplicationContext(), CrQ.class));
                        break;

                    case 1:
                        startActivity(new Intent(getApplicationContext(), AlQ.class));
                        break;

                    case 2:
                        startActivity(new Intent(getApplicationContext(), CrT.class));
                        break;
                    case 3:
                       startActivity(new Intent(getApplicationContext(), AlT.class));
                        break;

                    case 4:
                        startActivity(new Intent(getApplicationContext(), ReT.class));
                        break;


                    case 5:
                        startActivity(new Intent(getApplicationContext(), AlE.class));
                        break;


                    case 6:
                        startActivity(new Intent(getApplicationContext(), EEn.class));
                        break;

                    case 7:
                        startActivity(new Intent(getApplicationContext(), Stat.class));
                        break;

                }

            }
        });


        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction().add(R.id.container, new PlaceholderFragment()).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.espace_enseignant, menu);
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
                    R.layout.fragment_espace_enseignant, container, false);
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
                setUrl("bienvenueEnseignant.php");
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
            //Parse les donnÃ©es JSON
            try{
                JSONArray jArray = new JSONArray(result);
                for(int i=0;i<jArray.length();i++){
                    JSONObject json_data = jArray.getJSONObject(i);
                    // Affichage ID_question et Nom_question dans le LogCat
                    Log.i("log_tag","nom: "+json_data.getString("nom")+
                                    ", prenom: "+json_data.getString("prenom")
                    );
                    // Résultats de la requéte
                    returnString += "\n\t"+ json_data.getString("nom")+" "+json_data.getString("prenom");
                }
            }

            catch(JSONException e){
                Log.e("log_tag", "Error parsing data " + e.toString());
            }

            return returnString;
        }

        protected void onPostExecute(String returnString) {
            strs=returnString.split("\n\t");
            bienvenue.setText("You're welcom dear teacher" +
                    ""+strs[0]+" "+strs[1]);
        }
    }

}


