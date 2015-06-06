package weal.exemple.com.myprojectenseignant;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by WIN on 31/05/2015.
 */
public class CrT extends Activity {
    int  jour, mois, annee;
    private TabHost myTabHost;
    EditText matieretestInput, niveautestInput, seancetestInput, dureetestInput, coursprepareInput;
    String matieretestStr, datetestStr, dureetestStr, niveautestStr, seancetestStr, coursprepareStr;
    Button ChoixQuestsBtn, viderUn, viderDeux;
    DatePicker datetestInput;
    public static ArrayList<String> inputs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creer_test);
        inputs= new ArrayList<String>();

        matieretestInput= (EditText) findViewById(R.id.matieretestInput);
        niveautestInput= (EditText) findViewById(R.id.niveautestInput);
        seancetestInput= (EditText) findViewById(R.id.seancetestInput);
        datetestInput= (DatePicker) findViewById(R.id.date);
        jour=datetestInput.getDayOfMonth();
        mois= datetestInput.getMonth()+1;
        annee= datetestInput.getYear();
        final Calendar calendar = Calendar.getInstance();
        annee = calendar.get(Calendar.YEAR);
        mois = calendar.get(Calendar.MONTH);
        jour = calendar.get(Calendar.DAY_OF_MONTH);
        datetestInput.init(annee, mois, jour, null);

        dureetestInput= (EditText) findViewById(R.id.dureetestInput);
        coursprepareInput= (EditText) findViewById(R.id.coursprepareInput);

        viderDeux= (Button) findViewById(R.id.vider2creetestBtn);
        viderDeux.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {

                matieretestInput.setText("");
                niveautestInput.setText("");
                seancetestInput.setText("");
                dureetestInput.setText("");
                coursprepareInput.setText("");

            }
        });
        ChoixQuestsBtn= (Button) findViewById(R.id.choixquestBtn);
        ChoixQuestsBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                jour=datetestInput.getDayOfMonth();
                mois= datetestInput.getMonth()+1;
                annee= datetestInput.getYear();
                datetestStr= jour+"-"+mois+"-"+annee;

                matieretestStr = matieretestInput.getText().toString();
                seancetestStr = seancetestInput.getText().toString();
                dureetestStr = dureetestInput.getText().toString();
                niveautestStr =niveautestInput.getText().toString();
                coursprepareStr = coursprepareInput.getText().toString();

                if ((matieretestStr != null) && (matieretestStr.trim().length() > 0) &&
                        (niveautestStr != null) && (niveautestStr.trim().length() > 0)
                        && (seancetestStr != null) && (seancetestStr.trim().length() > 0) &&
                        (datetestStr != null) && (datetestStr.trim().length() > 0)
                        && (dureetestStr != null) && (dureetestStr.trim().length() > 0) &&
                        (coursprepareStr != null) && (coursprepareStr.trim().length() > 0)) {

                    startActivity(new Intent(getApplicationContext(), ChQuT.class));
                    Toast.makeText(getApplicationContext(), "Sélectionner les questions du test", Toast.LENGTH_LONG).show();

                    inputs.add(matieretestStr);
                    inputs.add(niveautestStr);
                    inputs.add(seancetestStr);
                    inputs.add(datetestStr);
                    inputs.add(dureetestStr);
                    inputs.add(coursprepareStr);

                    Intent choixIntent = new Intent(CrT.this, ChQuT.class);
                    String str = inputs.toString();
                    choixIntent.putExtra("quests",str );
                    inputs.clear();
                    startActivityForResult(choixIntent, 90);
                    CrT.this.finish();
                }
                else Toast.makeText(getApplicationContext(), "Tous les champs sont obligatoires !!! !", Toast.LENGTH_LONG).show();

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
        getMenuInflater().inflate(R.menu.creer_test, menu);
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
            View rootView = inflater.inflate(R.layout.fragment_creer_test,
                    container, false);
            return rootView;
        }
    }

}

