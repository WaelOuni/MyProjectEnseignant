package weal.exemple.com.myprojectenseignant;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
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
public class ReT extends Activity {
    Button voirResult, confirm;
    DatePicker datInput;
    EditText seaInput, matInput;
    String seaStr="",datStr="", matStr="", txt;
    ListView lv;
    ArrayAdapter<String> adap;
    static String[] strs;
    int id, jour, mois, annee;

    static final int DATE_DIALOG_ID = 100;

    //Building Parameters
    List<NameValuePair> params = new ArrayList<NameValuePair>();
    public static ArrayList<String> inputs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultat_test);
        inputs= new ArrayList<String>();
        datInput=(DatePicker) findViewById(R.id.date);
        seaInput= (EditText) findViewById(R.id.seanceresultInput);
        matInput= (EditText) findViewById(R.id.matiereresultInput);

        jour=datInput.getDayOfMonth();
        mois= datInput.getMonth()+1;
        annee= datInput.getYear();
        final Calendar calendar = Calendar.getInstance();
        annee = calendar.get(Calendar.YEAR);
        mois = calendar.get(Calendar.MONTH);
        jour = calendar.get(Calendar.DAY_OF_MONTH);
        datInput.init(annee, mois, jour, null);
        confirm= (Button) findViewById(R.id.confirmBtn);
        confirm.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                jour=datInput.getDayOfMonth();
                mois= datInput.getMonth()+1;
                annee= datInput.getYear();
                datStr= jour+"-"+mois+"-"+annee;
                params.add(new BasicNameValuePair("seance", seaInput.getText().toString()));
                params.add(new BasicNameValuePair("date", datStr));
                params.add(new BasicNameValuePair("matiere", matInput.getText().toString()));
                //Toast.makeText(getApplicationContext(), datStr, Toast.LENGTH_LONG).show();
                DownloadTask dlTask = new DownloadTask();
                dlTask.execute();
                confirm.setVisibility(View.INVISIBLE);
            }
        });





        voirResult= (Button) findViewById(R.id.resultBtn);

        voirResult.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                jour=datInput.getDayOfMonth();
                mois= datInput.getMonth();
                annee= datInput.getYear();
                datStr= jour+""+mois+""+annee;
                seaStr= seaInput.getText().toString();
                matStr= matInput.getText().toString();
                if ((seaStr != null) && (seaStr.trim().length() > 0) &&
                        (datStr!= null) && (datStr.trim().length() > 0)&&
                        (matStr!= null) && (matStr.trim().length() > 0)) {

                    Intent choixIntent = new Intent(getApplicationContext(), VRe.class);
                    //String str = inputs.toString();
                    choixIntent.putExtra("identifiant",id );
                    inputs.clear();

                    startActivityForResult(choixIntent,80);
                    finish();

                }
                else Toast.makeText(getApplicationContext(), "All fields are required !!!", Toast.LENGTH_LONG).show();

            }
        });
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment()).commit();
        }
    }
    public void addButtonListener() {

        //	 = (Button) findViewById(R.id.button);

        datInput.setOnClickListener(new View.OnClickListener() {

            @SuppressWarnings("deprecation")
            @Override
            public void onClick(View v) {

                showDialog(DATE_DIALOG_ID);

            }

        });

    }
    @Override
    protected Dialog onCreateDialog(int id) {

        switch (id) {
            case DATE_DIALOG_ID:
                // set date picker as current date
                return new DatePickerDialog(this, datePickerListener, annee, mois,jour);
        }
        return null;
    }


    private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        public void onDateSet(DatePicker view, int selectedannee,int selectedmois, int selectedjour) {
            annee = selectedannee;
            mois = selectedmois;
            jour = selectedjour;

			/* set selected date into Text View
			text_date.setText(new StringBuilder().append(mois + 1)
			   .append("-").append(jour).append("-").append(annee).append(" "));

			 set selected date into Date Picker*/
            datInput.init(annee, mois, jour, null);

        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.resultat_test, menu);
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
            View rootView = inflater.inflate(R.layout.fragment_resultat_test,
                    container, false);
            return rootView;
        }
    }


    public void setUrl(String newstr){
        strURL +=newstr;
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
            //strs=returnString.split("\n\t");
            returnString=String.valueOf(id);
            Toast.makeText(ReT.this,returnString , Toast.LENGTH_LONG).show();
        }
    }

}

