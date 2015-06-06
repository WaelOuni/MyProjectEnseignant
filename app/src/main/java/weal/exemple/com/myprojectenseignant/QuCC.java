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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by WIN on 31/05/2015.
 */
public class QuCC extends Activity {
    private TabHost myTabHost;
    Button vider, vider2, enregistrer;
    EditText ennonce, reponse, ch1, ch2, ch3;
    RadioButton espaceEns, nouvQuesion;
    String ennonceStr ,reponseStr ,ch1Str,ch2Str,ch3Str;
    String somme;
    static String[] strs;

    //Building Parameters
    List<NameValuePair> params = new ArrayList<NameValuePair>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_case_cocher);

        vider= (Button) findViewById(R.id.vidercasecocherBtn);
        vider2= (Button)findViewById(R.id.vider2questBtn);
        enregistrer= (Button) findViewById(R.id.enregistrerquestBtn);
        espaceEns=(RadioButton) findViewById(R.id.espaceensRadio1);
        nouvQuesion= (RadioButton) findViewById(R.id.nouvquestioncbRadio1);

        ennonce= (EditText) findViewById(R.id.enonceccInput);
        reponse= (EditText) findViewById(R.id.reponseccInput);
        ch1= (EditText) findViewById(R.id.choixA_Input);
        ch2= (EditText) findViewById(R.id.choixB_Input);
        ch3= (EditText) findViewById(R.id.choixC_Input);


        String newString;
        char[] str;


        Bundle  extras = getIntent().getExtras();
        if(extras == null) {
            newString= null;
        } else {

            String source=getCallingActivity().getShortClassName();
            source=source.substring(1);

            newString= (String) extras.getString("quests");

            somme="";
            str= new char[newString.length()-2];
            newString.getChars(1, newString.length()-1, str, 0);// pour elliminer les []
            for (int i=0; i<newString.length()-2;i++){
                somme=somme+str[i];
            }

            //Toast.makeText(getApplicationContext(), somme, Toast.LENGTH_LONG).show();
            strs=somme.split(",");
            Toast.makeText(getApplicationContext(), strs[0]+" "+strs[1], Toast.LENGTH_LONG).show();

        }




        vider.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                ennonce.setText("");
                reponse.setText("");

            }
        });
        vider2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                ch1.setText("");
                ch2.setText("");
                ch3.setText("");
            }
        });

        enregistrer.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                ennonceStr = ennonce.getText().toString();
                reponseStr =reponse.getText().toString();
                ch1Str = ch1.getText().toString();
                ch2Str = ch2.getText().toString();
                ch3Str = ch3.getText().toString();
                Intent i = new Intent(getApplicationContext(), EEn.class);
                if ((ennonceStr != null) && (ennonceStr.trim().length() > 0) && (reponseStr != null) && (reponseStr.trim().length() > 0)
                        && (ch1Str != null) && (ch1Str.trim().length() > 0) && (ch2Str != null) && (ch2Str.trim().length() > 0)
                        && (ch3Str != null) && (ch3Str.trim().length() > 0) ) {

                    params.add(new BasicNameValuePair("enoncecc", ennonce.getText().toString()));
                    params.add(new BasicNameValuePair("reponsecc", reponse.getText().toString()));
                    params.add(new BasicNameValuePair("choixa", ch1.getText().toString()));
                    params.add(new BasicNameValuePair("choixb", ch2.getText().toString()));
                    params.add(new BasicNameValuePair("choixc", ch3.getText().toString()));
                    params.add(new BasicNameValuePair("matierecc", strs[0]));
                    params.add(new BasicNameValuePair("niveaucc", strs[1]));
                    DownloadTask dlTask = new DownloadTask();
                    dlTask.execute();

                    if (espaceEns.isChecked()){
                        i = new Intent(getApplicationContext(), EEn.class);

                    }
                    else if (nouvQuesion.isChecked()){
                        i = new Intent(getApplicationContext(), CrQ.class);
                    }
                    startActivity(i);
                    finish();
                }else Toast.makeText(getApplicationContext(), "Tous les champs sont obligatoires !!! !", Toast.LENGTH_LONG).show();

            }
        });

        myTabHost =(TabHost) findViewById(android.R.id.tabhost);
        // Before adding tabs, it is imperative to call the method setup()
        myTabHost.setup();

        myTabHost.addTab(myTabHost.newTabSpec("tab_a").setIndicator("etape 1",getResources().getDrawable(android.R.drawable.ic_menu_view)).setContent(R.id.etape_1));
        myTabHost.addTab(myTabHost.newTabSpec("tab_b").setIndicator("etape 2",getResources().getDrawable(android.R.drawable.ic_menu_view)).setContent(R.id.etape_2));



        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment()).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.question_case_cocher, menu);
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
                    R.layout.fragment_question_case_cocher, container, false);
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

            // Envoie de la commande http
            try{
                HttpClient httpclient = new DefaultHttpClient();
                setUrl("creerquestcasecocher.php");
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
            }
            catch(Exception e){
                Log.e("log_tag", "Error converting result to string " + e.toString());
            }

            return returnString;
        }

        protected void onPostExecute(String returnString) {
            //strs=returnString.split("\n\t");

            Toast.makeText(QuCC.this,"Ajout d'une nouvelle question case à cocher avec succes" , Toast.LENGTH_LONG).show();
        }

    }


}

