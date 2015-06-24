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
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

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
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

/**
 * Created by WIN on 31/05/2015.
 */
public class AlT extends Activity {
    ArrayList<Test> tests;
    ListView listTests;
    String[] strs, ids,matieres, niveaux, seances, dates, durees;
    String somme;
    private Socket socket;

    private static final int SERVERPORT = 5000;
    private static final String SERVER_IP = "10.0.3.2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_tests);

        tests= new ArrayList<Test>();
        listTests= (ListView) this.findViewById(R.id.listTests);

        DownloadTask dlTask = new DownloadTask();
        dlTask.execute();

        new Thread(new ClientThread()).start();

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment()).commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.all_tests, menu);
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
            View rootView = inflater.inflate(R.layout.fragment_all_tests,
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
                setUrl("alltests.php");
                HttpPost httppost = new HttpPost(strURL);
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                is = entity.getContent();
            }
            catch(Exception e){
                Log.e("log_tag", "Error in http connection " + e.toString());
            }
            // Convertion de la requete en string
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
                    Log.i("log_tag","id: "+json_data.getInt("id_test") +
                                    ", matiere: "+json_data.getString("subject_test")+
                                    ", niveau: "+json_data.getString("level_test")+
                                    ", seance: "+json_data.getString("session_test")+
                                    ", date: "+json_data.getString("date_test")+
                                    ", duree: "+json_data.getString("duration_test")
                    );
                    // RÃ©sultats de la requÃªte

                    returnString += "\n\t"+json_data.getInt("id_test")+"." + json_data.getString("subject_test")+".." + json_data.getString("level_test")
                            +"..." + json_data.getString("session_test")+"...." + json_data.getString("date_test")+"....." + json_data.getString("duration_test");
                }
            }
            catch(JSONException e){
                Log.e("log_tag", "Error parsing data " + e.toString());
            }
            return returnString;
        }

        protected void onPostExecute(String returnString) {
            strs=returnString.split("\n\t");

            ids= new String[strs.length];
            for(int i=0;i< strs.length;i++){
                ids[i]=strs[i];
            }
            for(int i=1;i< ids.length;i++){
                String chaine = new String();
                // mettre l'ids dans une chaine de caractére
                chaine=ids[i].substring(0,ids[i].indexOf(".") );
                ids[i]=chaine;
            }	//extraire les ids a partir les tetss
            matieres= new String[strs.length];
            for(int i=0;i< strs.length;i++){
                matieres[i]=strs[i];
            }
            for(int i=1;i< matieres.length;i++){
                String chaine = new String();
                // mettre la matiere dans une chaine de caractére
                chaine=matieres[i].substring(matieres[i].indexOf(".")+1,matieres[i].indexOf("..") );
                matieres[i]=chaine;
            }	//extraire les matieres a partir les tests
            niveaux= new String[strs.length];
            for(int i=0;i< strs.length;i++){
                niveaux[i]=strs[i];
            }
            for(int i=1;i< niveaux.length;i++){
                String chaine = new String();
                // mettre niveau dans une chaine de caractére
                chaine=niveaux[i].substring(niveaux[i].indexOf("..")+2,niveaux[i].indexOf("...") );
                niveaux[i]=chaine;
            }	//extraire les niveaux a partir les tests
            seances= new String[strs.length];
            for(int i=0;i< strs.length;i++){
                seances[i]=strs[i];
            }
            for(int i=1;i< seances.length;i++){
                String chaine = new String();
                // mettre seance dans une chaine de caractére
                chaine=seances[i].substring(seances[i].indexOf("...")+3,seances[i].indexOf("....") );
                seances[i]=chaine;
            }	//extraire les seances a partir les tests
            dates= new String[strs.length];
            for(int i=0;i< strs.length;i++){
                dates[i]=strs[i];
            }
            for(int i=1;i< dates.length;i++){
                String chaine = new String();
                // mettre date dans une chaine de caractére
                chaine=dates[i].substring(dates[i].indexOf("....")+4,dates[i].indexOf(".....") );
                dates[i]=chaine;
            }	//extraire les dates a partir les tests
            durees= new String[strs.length];
            for(int i=0;i< strs.length;i++){
                durees[i]=strs[i];
            }
            for(int i=1;i< durees.length;i++){
                String chaine = new String();
                // mettre duree dans une chaine de caractére
                chaine=durees[i].substring(durees[i].indexOf(".....")+5,strs[i].length() );
                durees[i]=chaine;
            }	//extraire les durees a partir les tests

            for(int i=1;i< strs.length;i++){
                Test t= new Test(Integer.parseInt(ids[i]), matieres[i], niveaux[i], seances[i], dates[i], durees[i]);
                tests.add(t);
            }

            MyAdapterAlltests adapt = new MyAdapterAlltests(AlT.this,tests);
            //listlelec.setAdapter(adaptElect);
            listTests.setAdapter(adapt);


            listTests.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

                @Override
                public boolean onItemLongClick(AdapterView<?> arg0,
                                               View arg1, int arg2, long arg3) {

                    Toast.makeText(AlT.this, arg2 + 1 + " est selectionné ", Toast.LENGTH_LONG).show();
                    //new Thread(new Runnable() {
                    try {
                        PrintWriter out = new PrintWriter(new BufferedWriter(
                                new OutputStreamWriter(socket.getOutputStream())),
                                true);
                        out.println(arg2+1);
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return true;
                }
            });

        }
    }


    class ClientThread implements Runnable {

        @Override
        public void run() {

            try {
                InetAddress serverAddr = InetAddress.getByName(SERVER_IP);

                socket = new Socket(serverAddr, SERVERPORT);
                System.out.println("test Cliennnnnnnnnnntttt \n");

            } catch (UnknownHostException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }

        }

    }


}

