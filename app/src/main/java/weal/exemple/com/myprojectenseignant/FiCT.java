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
import android.widget.TextView;
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
public class FiCT extends Activity {
    TextView mat, niv, sea, dat, dur, cou, que;
    Button terminer;
    String somme;
    static String[] strs;
    static String[]str2;

    //Building Parameters
    List<NameValuePair> params = new ArrayList<NameValuePair>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fin_cree_test);
        mat= (TextView) findViewById(R.id.matTxt);
        niv= (TextView) findViewById(R.id.nivTxt);
        sea= (TextView) findViewById(R.id.seaTxt);
        dat= (TextView) findViewById(R.id.datTxt);
        dur= (TextView) findViewById(R.id.durTxt);
        cou= (TextView) findViewById(R.id.couTxt);
        que= (TextView) findViewById(R.id.queTxt);

        String newString, newString2;
        char[] str;

        Bundle  extras = getIntent().getExtras();
        if(extras == null) {
            newString= null;
            newString2= null;
        } else {

            String source=getCallingActivity().getShortClassName(); //fonctionne ssi il existe startActivityforresult() dans l'activity sender
            source=source.substring(1);

            newString= (String) extras.getString("env");
            newString2= (String) extras.getString("env2");

            str2=newString2.split(",");
            mat.setText(str2[0]);
            niv.setText(str2[1]);
            sea.setText(str2[2]);
            dat.setText(str2[3]);
            dur.setText(str2[4]);
            cou.setText(str2[5]);

            if (source.equals("ChoixQuestionsTest")){
                somme="";
                str= new char[newString.length()-2];
                newString.getChars(1, newString.length()-1, str, 0);// pour elliminer les []
                for (int i=0; i<newString.length()-2;i++){
                    somme=somme+str[i];
                }

                Toast.makeText(getApplicationContext(), somme, Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(), newString2, Toast.LENGTH_LONG).show();
                que.setText(somme);

            }

            terminer= (Button) findViewById(R.id.termincreeTestBtn);
            terminer.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {

                    //	Toast.makeText(FinCreeTest.this,s , Toast.LENGTH_LONG).show();
                    params.add(new BasicNameValuePair("matiere", str2[0]));
                    params.add(new BasicNameValuePair("niveau", str2[1]));
                    params.add(new BasicNameValuePair("seance", str2[2]));
                    params.add(new BasicNameValuePair("date", str2[3]));
                    params.add(new BasicNameValuePair("duree", str2[4]));
                    params.add(new BasicNameValuePair("coursprepare", str2[5]));
                    params.add(new BasicNameValuePair("numquestchoisis", somme));
                    DownloadTask dlTask = new DownloadTask();
                    dlTask.execute();

                    startActivity(new Intent(getApplicationContext(), EEn.class));
                    FiCT.this.finish();

                }
            });


            if (savedInstanceState == null) {
                getFragmentManager().beginTransaction()
                        .add(R.id.container, new PlaceholderFragment()).commit();
            }
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.fin_cree_test, menu);
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
            View rootView = inflater.inflate(R.layout.fragment_fin_cree_test,
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
                setUrl("creertest.php");
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

            Toast.makeText(FiCT.this, "Ajout d'un nouveau test avec succes", Toast.LENGTH_LONG).show();
        }

    }



}

