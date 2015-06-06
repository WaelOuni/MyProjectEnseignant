package weal.exemple.com.myprojectenseignant;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

import org.achartengine.ChartFactory;
import org.achartengine.model.CategorySeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;
/**
 * Created by WIN on 31/05/2015.
 */
public class MyCharteTestStat {
    public Intent createIntent(Context context) {
        int[] colors = new int[] { Color.RED, Color.YELLOW, Color.BLUE, Color.GREEN};
        DefaultRenderer renderer = buildCategoryRenderer(colors);
        CategorySeries categorySeries = new CategorySeries("notes de Test");
        categorySeries.add("15-20 ", StatT.quinzevingt);
        categorySeries.add("10-15", StatT.dixquinze);
        categorySeries.add("5-10 ", StatT.cinqdix);
        categorySeries.add("0-5 ", StatT.zerocinq);
        StatT.quinzevingt=0;
        StatT.dixquinze=0;
        StatT.cinqdix=0;
        StatT.zerocinq=0;

        return ChartFactory.getPieChartIntent(context, categorySeries, renderer,"Statistique selon les notes de test");
    }

    private DefaultRenderer buildCategoryRenderer(int[] colors) {
        DefaultRenderer renderer = new DefaultRenderer();
        for (int color : colors) {
            SimpleSeriesRenderer r = new SimpleSeriesRenderer();
            r.setColor(color);
            renderer.addSeriesRenderer(r);

        }
        return renderer;
    }
}
