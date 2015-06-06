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
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Created by WIN on 31/05/2015.
 */
public class ChQuT extends Activity {
    ListView lv;
    String[] strs, ids, enonces, matieres;
    ArrayAdapter<String> adap;
    static ArrayList<Question> quest = new ArrayList<Question>();
    Button validerChoixBtn;
    String somme;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choix_questions_test);
        //final ListView listlelec = (ListView) this.findViewById(R.id.list_item);

        lv = (ListView) findViewById(R.id.listquestschioxTest);
        DownloadTask dlTask = new DownloadTask();
        dlTask.execute();

        String newString;
        char[] str;


        Bundle  extras = getIntent().getExtras();
        if(extras == null) {
            newString= null;
        } else {

            String source=getCallingActivity().getShortClassName();
            source=source.substring(1);

            newString= (String) extras.getString("quests");

            if (source.equals("CreerTest")){
                somme="";
                str= new char[newString.length()-2];
                newString.getChars(1, newString.length()-1, str, 0);// pour elliminer les []
                for (int i=0; i<newString.length()-2;i++){
                    somme=somme+str[i];
                }

                Toast.makeText(getApplicationContext(), somme, Toast.LENGTH_LONG).show();

            }

        }


        validerChoixBtn= (Button) findViewById(R.id.validerchoixBtn);
        validerChoixBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent CreeIntent = new Intent(ChQuT.this, FiCT.class);
                String str = MyAdapterChoixquest.list.toString();
                CreeIntent.putExtra("env",str );
                CreeIntent.putExtra("env2",somme );

                MyAdapterChoixquest.list.clear();
                startActivityForResult(CreeIntent, 111);
                ChQuT.this.finish();


            }
        });




        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment()).commit();
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
                setUrl("allquests.php");
                HttpPost httppost = new HttpPost(strURL);
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
                    Log.i("log_tag","id: "+json_data.getInt("id") +
                                    ", enonce: "+json_data.getString("enonce") +
                                    ", matiere: "+json_data.getString("matiere")
                    );
                    // RÃ©sultats de la requÃªte
                    String enoncestr="";
                    enoncestr=String.copyValueOf(json_data.getString("enonce").toCharArray());
                    enoncestr+="...";
                    returnString += "\n\t"+json_data.getInt("id")+"- "+enoncestr+ " " + json_data.getString("matiere");
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

            ids= new String[strs.length];
            for(int i=0;i< strs.length;i++){
                ids[i]=strs[i];
            }


            for(int i=1;i< ids.length;i++){
                String chaine = new String();
                // mettre l'ennonce dans une chaine de caractére
                chaine=ids[i].substring(0,ids[i].indexOf("-") );
                ids[i]=chaine;
            }	//extraire les matieres a partir les questions


            matieres= new String[strs.length];
            for(int i=0;i< strs.length;i++){
                matieres[i]=strs[i];
            }

            for(int i=1;i< matieres.length;i++){
                String chaine = new String();
                // mettre la matiere dans une chaine de caractére
                chaine=matieres[i].substring(matieres[i].indexOf("...")+4,matieres[i].length() );
                matieres[i]=chaine;
            }	//extraire les matieres a partir les questions


            enonces= new String[strs.length];
            for(int i=0;i< strs.length;i++){
                enonces[i]=strs[i];
            }


            for(int i=1;i< enonces.length;i++){
                String chaine = new String();
                // mettre l'ennonce dans une chaine de caractére
                chaine=enonces[i].substring(enonces[i].indexOf("-")+2,enonces[i].indexOf("...") );
                enonces[i]=chaine;
            }	//extraire les matieres a partir les questions

            for(int i=1;i< strs.length;i++){
                Question q= new Question(Integer.parseInt(ids[i]), enonces[i], matieres[i]);
                quest.add(q);
            }


            MyAdapterChoixquest adapt = new MyAdapterChoixquest(ChQuT.this,quest);
            //listlelec.setAdapter(adaptElect);

            lv.setAdapter(adapt);


        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.choix_questions_test, menu);
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
                    R.layout.fragment_choix_questions_test, container, false);
            return rootView;
        }
    }

}

