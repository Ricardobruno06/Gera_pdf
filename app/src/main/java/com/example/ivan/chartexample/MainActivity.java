package com.example.ivan.chartexample;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View.OnClickListener;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ICandleDataSet;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.itextpdf.text.Document;
//import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;


import static com.itextpdf.text.pdf.ICC_Profile.getInstance;

public class MainActivity extends AppCompatActivity implements OnClickListener {

    private LineChart lineChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button save = (Button) findViewById(R.id.button_create);
        save.setOnClickListener(this);
        Button save2 = (Button) findViewById(R.id.button_create2);
        save2.setOnClickListener(this);

        lineChart = (LineChart) findViewById(R.id.chart);
        constructGraph();



    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_create:
                if (lineChart.saveToGallery("grap", 50)) {

                    Toast.makeText(getApplicationContext(), "Saving SUCCESSFUL!",
                            Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(getApplicationContext(), "Saving FAILED!", Toast.LENGTH_SHORT)
                            .show();
                break;
            case R.id.button_create2:

                Document document = new Document();



                try {


                    Toast.makeText(getApplicationContext(), "Pdf OK1!",
                            Toast.LENGTH_SHORT).show();
                    PdfWriter.getInstance(document,
                            new FileOutputStream("/storage/emulated/0/relatorio"+ System.currentTimeMillis()+".pdf"));
                    document.open();

                    Paragraph paragraph = new Paragraph();
                    paragraph.setAlignment(Element.ALIGN_CENTER);
                    paragraph.add(new Phrase("Relatório"));
                    document.add(paragraph);

                    Toast.makeText(getApplicationContext(), "Pdf OK2!",
                            Toast.LENGTH_SHORT).show();
                    com.itextpdf.text.Image image1 = com.itextpdf.text.Image.getInstance("/storage/emulated/0/DCIM/grap.jpg");
                    image1.scaleAbsolute(200f, 200f);
                    document.add(image1);
                    ///storage/emulated/0/
                    Toast.makeText(getApplicationContext(), "Pdf OK3!",
                            Toast.LENGTH_SHORT).show();


                    /*String imageUrl = "http://jenkov.com/images/" +
                            "20081123-20081123-3E1W7902-small-portrait.jpg";
                    Toast.makeText(getApplicationContext(), "Pdf OK3!",
                            Toast.LENGTH_SHORT).show();

                    com.itextpdf.text.Image image22 = com.itextpdf.text.Image.getInstance(new URL(imageUrl));
                    document.add(image22);*/




                    document.add(new Chunk("Este relatori apresenta os resultados do reste T feito entre o App1 e o App2 os resultados " +
                            "são expresos logo abaixo atraves do grafico."));

                    //pega hora

                    SimpleDateFormat dateFormat_hora = new SimpleDateFormat("HH:mm:ss");

                    Date data = new Date();

                    Calendar cal = Calendar.getInstance();
                    cal.setTime(data);
                    Date data_atual = cal.getTime();

                    String hora_atual = dateFormat_hora.format(data_atual);
                    //

                    PdfPTable table = new PdfPTable(3); // 3 columns.

                    PdfPCell cell1 = new PdfPCell(new Paragraph("Cell 1"));
                    PdfPCell cell2 = new PdfPCell(new Paragraph("Cell 2"));
                    PdfPCell cell3 = new PdfPCell(new Paragraph("Cell 3"));
                    PdfPCell cell4 = new PdfPCell(new Paragraph(hora_atual));
                    table.addCell(new Paragraph("Text Mode"));
                    table.addCell(new Paragraph("Text Mode"));

                    table.addCell(cell1);
                    table.addCell(cell2);
                    table.addCell(cell3);
                    table.addCell(cell4);

                    document.add(table);


                    document.close();
                } catch(Exception e){
                    e.printStackTrace();
                }
                break;

            //case R.id.bSair:
              //  finish();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.candle, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.actionSave: {
                if (lineChart.saveToGallery("title" + System.currentTimeMillis(), 50)) {

                    Toast.makeText(getApplicationContext(), "Saving SUCCESSFUL!",
                            Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(getApplicationContext(), "Saving FAILED!", Toast.LENGTH_SHORT)
                            .show();
                break;
            }
        }
        return true;
    }

    private void constructGraph() {
        Utils.verifyStoragePermissions(this);

        LineChart lineChart = (LineChart) findViewById(R.id.chart);

        float[] dataFromFile = Utils.getOutliers();

        // creating list of entry
        List<Entry> entries = new ArrayList<Entry>();
        for (int i =0; i < dataFromFile.length; i++)
            entries.add(new Entry( i, dataFromFile[i] ));

        LineDataSet dataSet = new LineDataSet(entries, "Outliers"); // add entries to dataset
        dataSet.setColor(Color.parseColor("#f4b942"));
        dataSet.setCircleColor(Color.RED);
        dataSet.setValueTextColor(Color.BLUE);
        dataSet.setDrawValues(false);

        LineData lineData = new LineData(dataSet);
        lineChart.setData(lineData);
        lineChart.invalidate(); // refresh


    }


}
