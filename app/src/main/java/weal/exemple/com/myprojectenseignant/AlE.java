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
import android.widget.ListView;

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
public class AlE extends Activity {
    ArrayList<Etudiant> etudiants;
    ListView listEtudiants;
    String[] strs, cins,noms, prenoms, genres, emails, niveaux, inscriptions, telephones;
    String somme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_etudiants);

        etudiants= new ArrayList<Etudiant>();
        listEtudiants= (ListView) this.findViewById(R.id.listAllEtudiants);
        DownloadTask dlTask = new DownloadTask();
        dlTask.execute();

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment()).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.all_etudiants, menu);
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
            View rootView = inflater.inflate(R.layout.fragment_all_etudiants,
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
                setUrl("alletudiants.php");
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
                    Log.i("log_tag","cin: "+json_data.getInt("cin") +
                                    ", nom: "+json_data.getString("nom")+
                                    ", prenom: "+json_data.getString("prenom")+
                                    ", genre: "+json_data.getString("genre")+
                                    ", email: "+json_data.getString("email")+
                                    ", niveau: "+json_data.getString("niveau")+
                                    ", inscription: "+json_data.getString("inscription")+
                                    ", telephone: "+json_data.getString("telephone")
                    );
                    // RÃ©sultats de la requÃªte

                    returnString += "\n\t"+json_data.getInt("cin")+"." + json_data.getString("nom")+".." + json_data.getString("prenom")
                            +"..." + json_data.getString("genre")+"...." + json_data.getString("email")+"....." + json_data.getString("niveau")
                            +"......" + json_data.getString("inscription")+"......." + json_data.getString("telephone"); 	}
            }
            catch(JSONException e){
                Log.e("log_tag", "Error parsing data " + e.toString());
            }
            return returnString;
        }

        protected void onPostExecute(String returnString) {
            strs=returnString.split("\n\t");

            cins= new String[strs.length];
            for(int i=0;i< strs.length;i++){
                cins[i]=strs[i];
            }


            for(int i=1;i< cins.length;i++){
                String chaine = new String();
                // mettre le cin dans une chaine de caractére
                chaine=cins[i].substring(0,cins[i].indexOf(".") );
                cins[i]=chaine;
            }	//extraire les cins




            noms= new String[strs.length];
            for(int i=0;i< strs.length;i++){
                noms[i]=strs[i];
            }

            for(int i=1;i< noms.length;i++){
                String chaine = new String();
                // mettre le dans une chaine de caractére
                chaine=noms[i].substring(noms[i].indexOf(".")+1,noms[i].indexOf("..") );
                noms[i]=chaine;
            }	//extraire les noms



            prenoms= new String[strs.length];
            for(int i=0;i< strs.length;i++){
                prenoms[i]=strs[i];
            }


            for(int i=1;i< prenoms.length;i++){
                String chaine = new String();
                // mettre niveau dans une chaine de caractére
                chaine=prenoms[i].substring(prenoms[i].indexOf("..")+2,prenoms[i].indexOf("...") );
                prenoms[i]=chaine;
            }	//extraire les prenom


            genres= new String[strs.length];
            for(int i=0;i< strs.length;i++){
                genres[i]=strs[i];
            }


            for(int i=1;i< genres.length;i++){
                String chaine = new String();
                // mettre genre dans une chaine de caractére
                chaine=genres[i].substring(genres[i].indexOf("...")+3,genres[i].indexOf("....") );
                genres[i]=chaine;
            }	//extraire les genres


            emails= new String[strs.length];
            for(int i=0;i< strs.length;i++){
                emails[i]=strs[i];
            }


            for(int i=1;i< emails.length;i++){
                String chaine = new String();
                // mettre email dans une chaine de caractére
                chaine=emails[i].substring(emails[i].indexOf("....")+4,emails[i].indexOf(".....") );
                emails[i]=chaine;
            }	//extraire les emails


            niveaux= new String[strs.length];
            for(int i=0;i< strs.length;i++){
                niveaux[i]=strs[i];
            }


            for(int i=1;i< niveaux.length;i++){
                String chaine = new String();
                // mettre niveau dans une chaine de caractére
                chaine=niveaux[i].substring(niveaux[i].indexOf(".....")+5,niveaux[i].indexOf("......") );
                niveaux[i]=chaine;
            }	//extraire les niveaux

            inscriptions= new String[strs.length];
            for(int i=0;i< strs.length;i++){
                inscriptions[i]=strs[i];
            }


            for(int i=1;i< inscriptions.length;i++){
                String chaine = new String();
                // mettre inscription dans une chaine de caractére
                chaine=inscriptions[i].substring(inscriptions[i].indexOf("......")+6,inscriptions[i].indexOf("......."));
                inscriptions[i]=chaine;
            }	//extraire les inscriptions

            telephones= new String[strs.length];
            for(int i=0;i< strs.length;i++){
                telephones[i]=strs[i];
            }


            for(int i=1;i< telephones.length;i++){
                String chaine = new String();
                // mettre telephone dans une chaine de caractére
                chaine=telephones[i].substring(telephones[i].indexOf(".......")+7,strs[i].length() );
                telephones[i]=chaine;
            }	//extraire les telephones




            for(int i=1;i< strs.length;i++){
                Etudiant e= new Etudiant(Integer.parseInt(cins[i]), noms[i], prenoms[i], genres[i], emails[i], niveaux[i], Integer.parseInt(inscriptions[i]), telephones[i]);
                etudiants.add(e);
            }


            MyAdapterAllEtudiants adapt = new MyAdapterAllEtudiants(AlE.this,etudiants);
            //listlelec.setAdapter(adaptElect);
            listEtudiants.setAdapter(adapt);
        }}
}

