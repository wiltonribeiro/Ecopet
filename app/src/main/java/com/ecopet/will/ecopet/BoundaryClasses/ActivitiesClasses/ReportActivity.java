package com.ecopet.will.ecopet.BoundaryClasses.ActivitiesClasses;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.ecopet.will.ecopet.ControlClasses.LoadingControl;
import com.ecopet.will.ecopet.ControlClasses.ReportControl;
import com.ecopet.will.ecopet.EntityClasses.DataClasses.Photo;
import com.ecopet.will.ecopet.R;

import java.util.List;

public class ReportActivity extends AppCompatActivity {

    public static Photo photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        final Spinner spinnerReport = findViewById(R.id.spinnerReport);
        LoadingControl loadingControl = new LoadingControl(ReportActivity.this);


        final ReportControl reportControl = new ReportControl(photo,loadingControl);
        adapterSpinner(spinnerReport,reportControl.getReportOption());

        findViewById(R.id.btnReport).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = spinnerReport.getSelectedItemPosition();
                if(pos>0)
                    reportControl.reportImage(pos);
                else
                    Toast.makeText(ReportActivity.this, "Selecione uma opção", Toast.LENGTH_SHORT).show();

            }
        });



    }

    private void adapterSpinner(Spinner spinner, List<String> list){
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(ReportActivity.this, R.layout.support_simple_spinner_dropdown_item, list);
        spinnerAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
    }
}
