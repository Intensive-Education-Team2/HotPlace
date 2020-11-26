package com.posturn.hotplace;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private ArrayList<ObjectPlace> objectPlaces = new ArrayList<ObjectPlace>();
    private ArrayList<ObjectCount> objectCounts = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final FirebaseFirestore db = FirebaseFirestore.getInstance();




        Button writeBtn= findViewById(R.id.read);
        writeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
/*플레이스 데이터 추가
                objectPlaces.add(new ObjectPlace("가로수길", 37.5212, 127.0229, "https://firebasestorage.googleapis.com/v0/b/hotplaceserver.appspot.com/o/place%2Fgarosugil.jpg?alt=media&token=75372fe7-d872-466a-a852-02786ec2034f", "신사역 주변" , 1));
                objectPlaces.add(new ObjectPlace("강남", 37.4979, 127.0276, "https://firebasestorage.googleapis.com/v0/b/hotplaceserver.appspot.com/o/place%2Fgangnam.jpg?alt=media&token=602b8306-9867-47e3-b4fa-2517841d99d4", "강남역주변", 2));
                objectPlaces.add(new ObjectPlace("건대", 37.5418, 127.0659, "https://firebasestorage.googleapis.com/v0/b/hotplaceserver.appspot.com/o/place%2Fkunkuk.jpg?alt=media&token=d06ca4fe-024f-4b51-b76c-7324f4f931ed", "설명" , 3));
                objectPlaces.add(new ObjectPlace("경리단길", 37.5388, 126.9876, "https://firebasestorage.googleapis.com/v0/b/hotplaceserver.appspot.com/o/place%2Fgyunglidangil.jpg?alt=media&token=481f1243-668e-4d3b-8d7a-61af0744e3d8", "설명" , 4));
                objectPlaces.add(new ObjectPlace("망원동", 37.5555, 126.9048, "https://firebasestorage.googleapis.com/v0/b/hotplaceserver.appspot.com/o/place%2Fmangwondong.jpg?alt=media&token=cd66afd3-bf7f-4243-8714-3f59b82414ce", "설명" , 5));
                objectPlaces.add(new ObjectPlace("삼청동", 37.5860, 126.9814, "https://firebasestorage.googleapis.com/v0/b/hotplaceserver.appspot.com/o/place%2Fsamcheongdong.jpg?alt=media&token=94314527-e01e-4dc3-bd7c-36f69ea4c4e0", "설명" , 6));
                objectPlaces.add(new ObjectPlace("성수동", 37.5417, 127.0562, "https://firebasestorage.googleapis.com/v0/b/hotplaceserver.appspot.com/o/place%2Fseongsoodong.jpg?alt=media&token=30917cd4-2497-4139-a2c9-bb079e234eeb", "설명" , 7));
                objectPlaces.add(new ObjectPlace("신림", 37.4844, 126.9298, "https://firebasestorage.googleapis.com/v0/b/hotplaceserver.appspot.com/o/place%2Fsinlim.jpg?alt=media&token=a5043d45-05fc-445c-b255-ccb5ca93c79c", "설명" , 8));
                objectPlaces.add(new ObjectPlace("신촌", 37.5563, 126.9365, "https://firebasestorage.googleapis.com/v0/b/hotplaceserver.appspot.com/o/place%2Fsinlim.jpg?alt=media&token=a5043d45-05fc-445c-b255-ccb5ca93c79c", "설명" , 9));
                objectPlaces.add(new ObjectPlace("연남동", 37.5617, 126.9230 , "https://firebasestorage.googleapis.com/v0/b/hotplaceserver.appspot.com/o/place%2Fyeonnamdong.jpg?alt=media&token=390bfd24-3cec-4015-8c48-287d52290d21", "설명" , 10));
                objectPlaces.add(new ObjectPlace("왕십리", 37.5626, 127.0387, "https://firebasestorage.googleapis.com/v0/b/hotplaceserver.appspot.com/o/place%2Fwangsibli.jpg?alt=media&token=aebf82fd-de25-4762-a0a2-3adb88f677e6", "설명" , 11));
                objectPlaces.add(new ObjectPlace("잠실", 37.5139, 127.1000 , "https://firebasestorage.googleapis.com/v0/b/hotplaceserver.appspot.com/o/place%2Fjamsil.jpg?alt=media&token=4fc3b8ca-ba08-422b-b9a2-b2807e77dfbb", "설명" , 12));
                objectPlaces.add(new ObjectPlace("한남동", 37.5383, 127.0011, "https://firebasestorage.googleapis.com/v0/b/hotplaceserver.appspot.com/o/place%2Fhannamdong.jpg?alt=media&token=bdc31c0e-5d2c-4be7-b5b8-05e22f8c32b9", "설명" , 13));
                objectPlaces.add(new ObjectPlace("합정", 37.5496, 126.9139, "https://firebasestorage.googleapis.com/v0/b/hotplaceserver.appspot.com/o/place%2Fhapjeong.jpg?alt=media&token=22b58049-7030-4c06-aaa0-942b597f08e1", "설명" , 14));
                objectPlaces.add(new ObjectPlace("해방촌", 37.5448, 126.9859, "https://firebasestorage.googleapis.com/v0/b/hotplaceserver.appspot.com/o/place%2Fhaebangchon.jpg?alt=media&token=3c85fa3c-7d33-4db2-bbb0-a9750227a085", "설명" , 15));
                for(int i=0; i<15; i++){
                    db.collection("HotPlace").document(objectPlaces.get(i).getName()).set(objectPlaces.get(i));
                }
 */

//카운트 데이터 추가
                Map<String, Object> nestedData = new HashMap<>();

                objectCounts.add(new ObjectCount("가로수길", 1400));
                objectCounts.add(new ObjectCount("강남", 1500));
                objectCounts.add(new ObjectCount("건대", 1300));
                objectCounts.add(new ObjectCount("경리단길", 1200));
                objectCounts.add(new ObjectCount("망원동", 1100));
                objectCounts.add(new ObjectCount("삼청동", 1000));
                objectCounts.add(new ObjectCount("성수동", 900));
                objectCounts.add(new ObjectCount("신림", 100));
                objectCounts.add(new ObjectCount("신촌", 700));
                objectCounts.add(new ObjectCount("연남동", 600));
                objectCounts.add(new ObjectCount("왕십리", 500));
                objectCounts.add(new ObjectCount("잠실", 400));
                objectCounts.add(new ObjectCount("한남동", 300));
                objectCounts.add(new ObjectCount("합정", 200));
                objectCounts.add(new ObjectCount("해방촌", 800));

                for(int i=0; i<15; i++) {
                    nestedData.put(Integer.toString(i), objectCounts.get(i));
                }
                db.collection("Test").document("2020-11-24").set(nestedData);

            }
        });

    }
}