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
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by WIN on 31/05/2015.
 */
public class CrQ extends Activity {
    EditText matiere, niveau;
    Button enregistrer, vider;
    RadioButton textType, qcmType;
    String matStr, nivStr;
    public static ArrayList<String> inputs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creer_question);
        matiere = (EditText) findViewById(R.id.matiereInput);
        niveau = (EditText) findViewById(R.id.nivetudInput);
        enregistrer = (Button) findViewById(R.id.enregcreerquestBtn);
        vider = (Button) findViewById(R.id.vidercreerquestBtn);
        textType = (RadioButton) findViewById(R.id.textRadio);
        qcmType = (RadioButton) findViewById(R.id.casecocherRadio);
        inputs = new ArrayList<String>();
        enregistrer.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                matStr = matiere.getText().toString();
                nivStr = niveau.getText().toString();

                if ((matStr != null) && (matStr.trim().length() > 0) && (nivStr != null) && (nivStr.trim().length() > 0)) {
                    Intent choixIntent = new Intent();
                    inputs.add(matStr);
                    inputs.add(nivStr);
                    String str = inputs.toString();

                    if (textType.isChecked()) {
                        choixIntent = new Intent(getApplicationContext(), QuT.class);
                    } else if (qcmType.isChecked()) {
                        choixIntent = new Intent(getApplicationContext(), QuCC.class);
                    } else
                        Toast.makeText(getApplicationContext(), "choisir un type !", Toast.LENGTH_LONG).show();

                    choixIntent.putExtra("quests", str);
                    inputs.clear();
                    startActivityForResult(choixIntent, 80);
                    finish();

                } else
                    Toast.makeText(getApplicationContext(), "Tous les champs sont obligatoires !!! !", Toast.LENGTH_LONG).show();

            }

            //Tous les champs sont obligatoires !!!
        });

        vider.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                matiere.setText("");
                niveau.setText("");
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
        getMenuInflater().inflate(R.menu.creer_question, menu);
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
            View rootView = inflater.inflate(R.layout.fragment_creer_question,
                    container, false);
            return rootView;
        }
    }
}



