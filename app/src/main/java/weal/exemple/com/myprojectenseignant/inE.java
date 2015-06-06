package weal.exemple.com.myprojectenseignant;

import android.app.Activity;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TabHost;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WIN on 06/06/2015.
 */
public class inE extends Activity {
    Button creercompte;
    EditText nom, prenom, cin, email, motpasse, grade, specialite, telephone;
    String nomStr, prenomStr, cinStr,genreStr, emailStr, motpasseStr, gradeStr, specialiteStr, telephoneStr;
    //Building Parameters
    List<NameValuePair> params;
    RadioButton homme, femme;
    private TabHost myTabHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscription__enseignant);

        nom= (EditText) findViewById(R.id.nomInput);
        prenom= (EditText) findViewById(R.id.prenomInput);
        cin= (EditText) findViewById(R.id.cinInput);
        motpasse= (EditText) findViewById(R.id.passwordInput);
        email= (EditText) findViewById(R.id.emailInput);
        grade= (EditText) findViewById(R.id.gradeInput);
        specialite= (EditText) findViewById(R.id.specialiteInput);
        telephone= (EditText) findViewById(R.id.numTelInput);
        homme=(RadioButton) findViewById(R.id.hommeRadio);
        femme= (RadioButton) findViewById(R.id.femmeRadio);
        params = new ArrayList<NameValuePair>();
        myTabHost =(TabHost) findViewById(android.R.id.tabhost);
        // Before adding tabs, it is imperative to call the method setup()
        myTabHost.setup();


        myTabHost.addTab(myTabHost.newTabSpec("tab_a").setIndicator("etape 1",getResources().getDrawable(android.R.drawable.ic_menu_view)).setContent(R.id.tab1));
        myTabHost.addTab(myTabHost.newTabSpec("tab_b").setIndicator("etape 2",getResources().getDrawable(android.R.drawable.ic_menu_view)).setContent(R.id.tab2));
        myTabHost.addTab(myTabHost.newTabSpec("tab_c").setIndicator("etape 3",getResources().getDrawable(android.R.drawable.ic_menu_view)).setContent(R.id.tab3));


        creercompte = (Button) findViewById(R.id.creercompteBtn);
        creercompte.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                nomStr = nom.getText().toString();
                prenomStr = prenom.getText().toString();
                cinStr = cin.getText().toString();

                if(homme.isChecked()){
                    genreStr= "homme";}
                else {genreStr="femme";}

                emailStr = email.getText().toString();
                motpasseStr = motpasse.getText().toString();
                gradeStr = grade.getText().toString();
                specialiteStr = specialite.getText().toString();
                telephoneStr = telephone.getText().toString();

                if ((nomStr != null) && (nomStr.trim().length() > 0) && (prenomStr != null) && (prenomStr.trim().length() > 0)
                        && (cinStr != null) && (cinStr.trim().length() > 0) && (genreStr != null) && (genreStr.trim().length() > 0)
                        && (emailStr != null) && (emailStr.trim().length() > 0)&& (motpasseStr != null) && (motpasseStr.trim().length() > 0)
                        && (gradeStr != null) && (gradeStr.trim().length() > 0)&& (specialiteStr != null) && (specialiteStr.trim().length() > 0)
                        && (telephoneStr != null) && (telephoneStr.trim().length() > 0)) {

                    params.add(new BasicNameValuePair("cin", cin.getText().toString()));
                    params.add(new BasicNameValuePair("nom", nom.getText().toString()));
                    params.add(new BasicNameValuePair("prenom", prenom.getText().toString()));
                    params.add(new BasicNameValuePair("genre", genreStr));
                    params.add(new BasicNameValuePair("motpasse", motpasse.getText().toString()));
                    params.add(new BasicNameValuePair("email", email.getText().toString()));
                    params.add(new BasicNameValuePair("grade", grade.getText().toString()));
                    params.add(new BasicNameValuePair("specialite", specialite.getText().toString()));
                    params.add(new BasicNameValuePair("telephone", telephone.getText().toString()));
                    DownloadTask dlTask = new DownloadTask();
                    dlTask.execute();
                    inE.this.finish();
                }else Toast.makeText(getApplicationContext(), "Tous les champs sont obligatoires !!! !", Toast.LENGTH_LONG).show();

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
        getMenuInflater().inflate(R.menu.inscription__enseignant, menu);
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
            View rootView = inflater
                    .inflate(R.layout.fragment_inscription__enseignant,
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

            // Envoie de la commande http
            try{
                HttpClient httpclient = new DefaultHttpClient();
                setUrl("inscrireEnseignant.php");
                HttpPost httppost = new HttpPost(strURL);
                httppost.setEntity(new UrlEncodedFormEntity(params));
                httpclient.execute(httppost);
            }
            catch(Exception e){
                Log.e("log_tag", "Error in http connection " + e.toString());
            }


            return returnString;
        }

        protected void onPostExecute(String returnString) {
            //strs=returnString.split("\n\t");

            Toast.makeText(inE.this,"Ajout d'un nouveau enseignant avec succes" , Toast.LENGTH_LONG).show();
        }

    }

}


