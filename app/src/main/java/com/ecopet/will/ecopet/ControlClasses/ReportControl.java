package com.ecopet.will.ecopet.ControlClasses;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.ecopet.will.ecopet.BoundaryClasses.ActivitiesClasses.HomeActivity;
import com.ecopet.will.ecopet.EntityClasses.DataClasses.Photo;
import com.ecopet.will.ecopet.EntityClasses.OthersClasses.FirebaseData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ServerValue;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by willrcneto on 27/05/18.
 */

public class ReportControl {
    private Photo photo;
    private LoadingControl loadingControl;
    private List<String> reportOption;

    public ReportControl(Photo photo,LoadingControl loadingControl) {
        this.photo = photo;
        this.loadingControl = loadingControl;
        this.reportOption = new ArrayList<>();
        fillListOptions();
    }

    private void fillListOptions(){
        reportOption.add("Selecionar opção");
        reportOption.add("Imagem possui conteúdo pornográfico");
        reportOption.add("Imagem é considerada ofensiva");
        reportOption.add("Imagem foge do objetivo da aplicação");
        reportOption.add("Não há nada na imagem");
    }


    private void addMyListReports(final int valueReport){
        FirebaseData.myRef.child("users").child(FirebaseData.currentUser.getUid())
                .child("data").child("reports").child(photo.getUid_photo()).setValue(photo.getUid_user()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    FirebaseData.currentUser.getReports().put(photo.getUid_photo(), photo.getUid_user());
                    addReportToImage(valueReport);
                } else{
                    loadingControl.finishLoading();
                    Toast.makeText(loadingControl.getActivity(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void addReportToImage(int valueReport){
        FirebaseData.myRef.child("photos").child(photo.getUid_photo()).child("reports").setValue(valueReport);
        if(valueReport>=6) {
            FirebaseData.myRef.child("tag_photo").child(photo.getTag().replace("#","")).child(photo.getUid_photo()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()) {
                        FirebaseData.myRef.child("users_reported").child(photo.getUid_user()).setValue(ServerValue.TIMESTAMP).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    loadingControl.getActivity().finishAffinity();
                                    Intent back = new Intent(loadingControl.getActivity(), HomeActivity.class);
                                    //back.putExtra("back","back");
                                    loadingControl.getActivity().startActivity(back);
                                }

                                else{
                                    loadingControl.finishLoading();
                                    Toast.makeText(loadingControl.getActivity(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } else{
                        loadingControl.finishLoading();
                        Toast.makeText(loadingControl.getActivity(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else{
            loadingControl.finishLoading();
            loadingControl.getActivity().finish();
        }
    }

    public void reportImage(int positionOption){
        loadingControl.startLoading();
        int valueReport = calculateReport(positionOption);
        addMyListReports(valueReport);
    }

    private int calculateReport(int positionOption){
        int valueReport = photo.getReports();
        if(positionOption == 1)
            valueReport+=6;
        else if(positionOption == 2)
            valueReport+=3;
        else if(positionOption == 3)
            valueReport+=2;
        else if(positionOption == 4)
            valueReport+=1;

        return valueReport;
    }

    public List<String> getReportOption() {
        return reportOption;
    }
}
