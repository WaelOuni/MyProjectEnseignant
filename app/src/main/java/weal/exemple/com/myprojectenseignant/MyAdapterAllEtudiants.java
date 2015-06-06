package weal.exemple.com.myprojectenseignant;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by WIN on 31/05/2015.
 */
public class MyAdapterAllEtudiants extends BaseAdapter {

    private ArrayList<Etudiant> etudiants;
    private LayoutInflater myInflater;

    public MyAdapterAllEtudiants (Context context, ArrayList<Etudiant> etudiants)
    {
        this.myInflater = LayoutInflater.from(context);
        this.etudiants = etudiants;
    }

    @Override
    public int getCount() {
        return this.etudiants.size();
    }

    @Override
    public Object getItem(int arg0) {
        return this.etudiants.get(arg0);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder {
        TextView text01;
        TextView text02;
        TextView text03;
        TextView text04;
        TextView text05;
        TextView text06;
        TextView text07;
        TextView text08;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null)
        {
            convertView = myInflater.inflate(R.layout.listitemalletudiants, null);
            holder = new ViewHolder();
            holder.text01 = (TextView) convertView.findViewById(R.id.cinAllEtud);
            holder.text02 = (TextView) convertView.findViewById(R.id.nomAllEtud);
            holder.text03 = (TextView) convertView.findViewById(R.id.prenomAllEtud);
            holder.text04 = (TextView) convertView.findViewById(R.id.genreAllEtuditants);
            holder.text05 = (TextView) convertView.findViewById(R.id.emailAllEtudiants);
            holder.text06 = (TextView) convertView.findViewById(R.id.niveauAllEtudiants);
            holder.text07 = (TextView) convertView.findViewById(R.id.inscriptionAllEtudiants);
            holder.text08 = (TextView) convertView.findViewById(R.id.telephoneAllEtudiants);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.text01.setText("CIN:"+Integer.toString(etudiants.get(position).cin));
        holder.text02.setText("NOM:"+etudiants.get(position).nom);
        holder.text03.setText("PRE:"+etudiants.get(position).prenom);
        holder.text04.setText(etudiants.get(position).genre);
        holder.text05.setText(etudiants.get(position).email);
        holder.text06.setText("NIV:"+etudiants.get(position).niveau);
        holder.text07.setText("INS:"+Integer.toString(etudiants.get(position).inscription));
        holder.text08.setText(etudiants.get(position).telephone);

        return convertView;

    }
}
