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
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
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
 * Created by WIN on 31/05/2015.
 */
public class QuT extends Activity {
    Button enregistrer, vider;
    RadioButton espaceens, nouvqt;
    EditText enonce, reponse;
    String enonceStr, repStr;
    String somme;
    static String[] strs;

    //Building Parameters
    List<NameValuePair> params = new ArrayList<NameValuePair>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_text);

        enonce = (EditText) findViewById(R.id.enoceInput);
        reponse = (EditText) findViewById(R.id.reponseInput);

        String newString;
        char[] str;


        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            newString = null;
        } else {

            String source = getCallingActivity().getShortClassName();
            source = source.substring(1);

            newString = (String) extras.getString("quests");


            if (source.equals("CrQ")) {
                somme = "";
                str = new char[newString.length() - 2];
                newString.getChars(1, newString.length() - 1, str, 0);// pour elliminer les []
                for (int i = 0; i < newString.length() - 2; i++) {
                    somme = somme + str[i];
                }
                Log.i("teeeeestt", "" + somme);
                Toast.makeText(getApplicationContext(), somme, Toast.LENGTH_LONG).show();
                strs = somme.split(",");

                Toast.makeText(getApplicationContext(), strs[0] + " " + strs[1], Toast.LENGTH_LONG).show();

            }

        }

        espaceens = (RadioButton) findViewById(R.id.espaceensqtRadio);
        nouvqt = (RadioButton) findViewById(R.id.nouvquestionqtRadio);

        enregistrer = (Button) findViewById(R.id.enregistrerqtBtn);
        vider = (Button) findViewById(R.id.viderqtBtn);
        enregistrer.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                enonceStr = enonce.getText().toString();
                repStr = reponse.getText().toString();

                if ((enonceStr != null) && (enonceStr.trim().length() > 0) && (repStr != null) && (repStr.trim().length() > 0)) {
                    Intent i = new Intent(getApplicationContext(), EEn.class);
                    params.add(new BasicNameValuePair("enonce", enonce.getText().toString()));
                    params.add(new BasicNameValuePair("reponse", reponse.getText().toString()));
                    params.add(new BasicNameValuePair("matiere", strs[0]));
                    params.add(new BasicNameValuePair("niveau", strs[1]));

                    DownloadTask dlTask = new DownloadTask();
                    dlTask.execute();

                    if (espaceens.isChecked()) {

                        i = new Intent(getApplicationContext(), EEn.class);

                    } else if (nouvqt.isChecked()) {
                        i = new Intent(getApplicationContext(), CrQ.class);
                    }

                    startActivity(i);
                    finish();


                }

            }
        });

        vider.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                enonce.setText("");
                reponse.setText("");
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
        getMenuInflater().inflate(R.menu.question_text, menu);
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
            View rootView = inflater.inflate(R.layout.fragment_question_text,
                    container, false);
            return rootView;
        }
    }


    public void setUrl(String newstr) {
        strURL += newstr;
    }

    public String strURL = "http://10.0.3.2/MyProjectConnect/Enseignant/";

    public class DownloadTask extends AsyncTask<UrlEncodedFormEntity, Void, String> {

        @Override
        protected String doInBackground(UrlEncodedFormEntity... urls) {

            String returnString = "";

            // Envoie de la commande http
            try {
                HttpClient httpclient = new DefaultHttpClient();
                setUrl("creerquesttext.php");
                HttpPost httppost = new HttpPost(strURL);
                httppost.setEntity(new UrlEncodedFormEntity(params));
                httpclient.execute(httppost);

            } catch (Exception e) {
                Log.e("log_tag", "Error in http connection " + e.toString());
            }


            return returnString;
        }

        protected void onPostExecute(String returnString) {
            //strs=returnString.split("\n\t");

            Toast.makeText(QuT.this, "Ajout d'une nouvelle question texte avec succes", Toast.LENGTH_LONG).show();
        }

    }

}

