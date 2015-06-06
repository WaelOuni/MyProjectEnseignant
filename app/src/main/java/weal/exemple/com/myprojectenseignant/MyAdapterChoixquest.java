package weal.exemple.com.myprojectenseignant;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import android.widget.CompoundButton.OnCheckedChangeListener;
import java.util.ArrayList;

/**
 * Created by WIN on 31/05/2015.
 */
public class MyAdapterChoixquest extends BaseAdapter implements OnCheckedChangeListener {


    public static ArrayList<Integer> list;
    boolean[] mCheckStates;
    public static int compt=0;
    private ArrayList<Question> quest;
    private LayoutInflater myInflater;

    public MyAdapterChoixquest (Context context, ArrayList<Question> quest)
    {
        this.myInflater = LayoutInflater.from(context);
        this.quest = quest;
        mCheckStates =new boolean[quest.size()];
        list = new ArrayList<Integer>();
        list.clear();
    }

    @Override
    public int getCount() {
        return this.quest.size();
    }

    @Override
    public Object getItem(int arg0) {
        return this.quest.get(arg0).id+this.quest.get(arg0).ennonce+this.quest.get(arg0).matiere;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder {
        TextView text01;
        TextView text02;
        TextView text03;
        CheckBox check;
        //	ImageView img;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null)

        {
            convertView = myInflater.inflate(R.layout.listitemchoixquest, null);

            holder = new ViewHolder();

            holder.text01 = (TextView) convertView.findViewById(R.id.idquest);

            holder.text02 = (TextView) convertView.findViewById(R.id.ennoncequest);

            holder.text03 = (TextView) convertView.findViewById(R.id.matierequest);

            holder.check= (CheckBox) convertView.findViewById(R.id.selectquest);

            convertView.setTag(holder);

        } else {

            holder = (ViewHolder) convertView.getTag();

        }

        holder.check.setTag(position);

        holder.check.setChecked(mCheckStates[position]);

        holder.check.setOnCheckedChangeListener(this);

        holder.text01.setText(Integer.toString(quest.get(position).id));

        holder.text02.setText(quest.get(position).ennonce);

        holder.text03.setText(quest.get(position).matiere);

        holder.check.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                compt++;

                if(((CheckBox)v).isChecked()){

                    mCheckStates[position]=true;

                    list.add(position+1);}
                else

                {mCheckStates[position]=false;

                    list.remove(list.get(position+1));

                }

            }

        });

        return convertView;

    }

    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }

}


