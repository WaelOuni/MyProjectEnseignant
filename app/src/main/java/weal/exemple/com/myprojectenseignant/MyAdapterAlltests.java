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
public class MyAdapterAlltests extends BaseAdapter {

    private ArrayList<Test> tests;
    private LayoutInflater myInflater;

    public MyAdapterAlltests (Context context, ArrayList<Test> tests)
    {
        this.myInflater = LayoutInflater.from(context);
        this.tests = tests;
    }

    @Override
    public int getCount() {
        return this.tests.size();
    }

    @Override
    public Object getItem(int arg0) {
        return this.tests.get(arg0);
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
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null)
        {
            convertView = myInflater.inflate(R.layout.listitemalltests, null);
            holder = new ViewHolder();
            holder.text01 = (TextView) convertView.findViewById(R.id.idtest);
            holder.text02 = (TextView) convertView.findViewById(R.id.mattest);
            holder.text03 = (TextView) convertView.findViewById(R.id.nivtest);
            holder.text04 = (TextView) convertView.findViewById(R.id.seatest);
            holder.text05 = (TextView) convertView.findViewById(R.id.dattest);
            holder.text06 = (TextView) convertView.findViewById(R.id.durtest);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.text01.setText("ID:"+Integer.toString(tests.get(position).id));
        holder.text02.setText("MAT:"+tests.get(position).mat);
        holder.text03.setText("NIV:"+tests.get(position).niv);
        holder.text04.setText("SEA:"+tests.get(position).sea);
        holder.text05.setText("DAT:"+tests.get(position).dat);
        holder.text06.setText("DUR:"+tests.get(position).dur+" minutes");

        return convertView;

    }

}

