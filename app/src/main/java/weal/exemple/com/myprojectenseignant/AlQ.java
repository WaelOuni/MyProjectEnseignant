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
public class AlQ extends Activity {
    ListView lv;
    ArrayAdapter<String> adap;
    Button filtrer;
    TextView txt;
    EditText matInput, nivInput;
    String matstr="", nivstr="", typeQuest="";
    static final String[] type= {"Texte","Case à cocher"};
    Spinner typQuestChoix;
    UrlEncodedFormEntity url1, url2, url3;
    static String[] strs,ids , enonces, matieres, permuts, strRetours;
    static ArrayList<Question> quest = new ArrayList<Question>();
    static ArrayList<String> niveaux = new ArrayList<String>();
    static ArrayList<String> reponses = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_quest_cc);

        matInput = (EditText) findViewById(R.id.matierequestsccInput);
        nivInput = (EditText) findViewById(R.id.niveauquestccInput);
        lv = (ListView) findViewById(R.id.allquestsLv);

        typQuestChoix = (Spinner) findViewById(R.id.typequestSpinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, type);
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        typQuestChoix.setAdapter(adapter);
        //Appeler la mÃ©thode pour rÃ©cupÃ©rer les donnÃ©es JSON
        DownloadTask dlTask = new DownloadTask();
        dlTask.execute();

        filtrer=(Button) findViewById(R.id.filtrerquestsccBtn);
        filtrer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                matstr=matInput.getText().toString();
                nivstr=nivInput.getText().toString();
                typeQuest=typQuestChoix.getSelectedItem().toString();
                filtreProcess(matstr, nivstr);
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

    void filtreProcess(String mat, String niv){


        if ((mat == null || (matstr.trim().length() == 0))  && (niv== null || (nivstr.trim().length() == 0)) ) {
            startActivity(new Intent(getApplicationContext(), getClass()));
            AlQ.this.finish();
        }

        if ((matstr != null) && (matstr.trim().length() > 0) || (nivstr!= null) && (nivstr.trim().length() > 0)) {

            if ((matstr != null) && (matstr.trim().length() > 0) && !((nivstr!= null) && (nivstr.trim().length() > 0))){
                permuts= new String[strs.length];

                for (int i=0; i<strs.length; i++){
                    permuts[i] = "";
                }

                int compt=0;
                for (int i=0; i<strs.length; i++){
                    if (matieres[i].contains(matstr)){
                        //compt++;
                        permuts[compt]=strs[i];
                        compt++;
                    }
                }

                if (compt==0)Toast.makeText(getApplicationContext(), "aucun donnée", Toast.LENGTH_SHORT).show();
                strRetours=new String[compt];
                for (int i=0; i<compt; i++){
                    strRetours[i]= permuts[i];
                }
            }

            if ((nivstr != null) && (nivstr.trim().length() > 0) && !((matstr!= null) && (matstr.trim().length() > 0))){
                permuts= new String[niveaux.size()];
                for (int i=0; i<niveaux.size(); i++){
                    permuts[i] = "";
                }

                int compt=0;
                for (int i=0; i<niveaux.size(); i++){
                    if (niveaux.get(i).contains(nivstr)){
                        //compt++;
                        permuts[compt]=strs[i+1];
                        compt++;
                    }
                }
                if (compt==0)Toast.makeText(getApplicationContext(), "aucun donnée", Toast.LENGTH_SHORT).show();
                strRetours=new String[compt];
                for (int i=0; i<compt; i++){
                    strRetours[i]= permuts[i];
                }
            }

            else {
                permuts= new String[niveaux.size()];
                for (int i=0; i<niveaux.size(); i++){
                    permuts[i] = "";
                }
                int compt=0;

                for (int i=0; i<niveaux.size(); i++){

                    if (matieres[i+1].contains(matstr)&& niveaux.get(i).contains(nivstr)){
                        //compt++;
                        permuts[compt]=strs[i+1];
                        compt++;
                    }
                }
                if (compt==0)Toast.makeText(getApplicationContext(), "aucun donnée", Toast.LENGTH_SHORT).show();

                strRetours=new String[compt];
                for (int i=0; i<compt; i++){
                    strRetours[i]= permuts[i];
                }
            }

            adap = new ArrayAdapter<String>(AlQ.this,
                    android.R.layout.simple_list_item_1, android.R.id.text1, strRetours);
            lv.setAdapter(adap);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1,
                                        int arg2, long arg3) {
                    Toast.makeText(getApplicationContext(),"liste filtré", Toast.LENGTH_LONG).show();
                }
            });

        }

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
                                    ", reponse: "+json_data.getString("reponse") +
                                    ", matiere: "+json_data.getString("matiere")+
                                    ", niveau: "+json_data.getString("niveau")
                    );
                    // RÃ©sultats de la requÃªte
                    String niveaustr="";
                    niveaustr=String.copyValueOf(json_data.getString("niveau").toCharArray());
                    niveaux.add(niveaustr);
                    String enoncestr="";
                    enoncestr=String.copyValueOf(json_data.getString("enonce").toCharArray());
                    enoncestr+="...";
                    String reponsestr="";
                    reponsestr=String.copyValueOf(json_data.getString("reponse").toCharArray());
                    reponses.add(reponsestr);
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
            adap = new ArrayAdapter<String>(AlQ.this,
                    android.R.layout.simple_list_item_1, android.R.id.text1, strs);
            lv.setAdapter(adap);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1,
                                        int arg2, long arg3) {
                    if ( lv.getAdapter().getCount()==strs.length);
                    Toast.makeText(getApplicationContext(),reponses.get(-1 + arg2), Toast.LENGTH_LONG).show();
                }
            });
            ids= new String[strs.length];
            for(int i=0;i< strs.length;i++){
                ids[i]=strs[i];
            }

            for(int i=1;i< ids.length;i++){
                String chaine = new String();
                // mettre l'id dans une chaine de caractére
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
            for(int i=1;i< enonces.length;i++){
                Question q= new Question( Integer.parseInt(ids[i]),enonces[i], matieres[i]);
                quest.add(q);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.all_quest_cc, menu);
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

    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {

        }
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_all_quest_cc,
                    container, false);
            return rootView;
        }
    }
}

