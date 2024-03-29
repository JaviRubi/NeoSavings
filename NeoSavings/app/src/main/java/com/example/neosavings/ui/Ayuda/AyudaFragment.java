package com.example.neosavings.ui.Ayuda;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.neosavings.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AyudaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AyudaFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AyudaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AyudaFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AyudaFragment newInstance(String param1, String param2) {
        AyudaFragment fragment = new AyudaFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_ayuda, container, false);
        CardView cardView=view.findViewById(R.id.Home_card);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),Ayuda_Home.class);
                intent.putExtra("CASO","HOME");
                startActivity(intent);
            }
        });
        cardView=view.findViewById(R.id.Users_Card);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),Ayuda_Home.class);
                intent.putExtra("CASO","USER");
                startActivity(intent);
            }
        });

        cardView=view.findViewById(R.id.Registros_Card);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),Ayuda_Home.class);
                intent.putExtra("CASO","REGISTROS");
                startActivity(intent);
            }
        });

        cardView=view.findViewById(R.id.Estadísticas_Card);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),Ayuda_Home.class);
                intent.putExtra("CASO","ESTADISTICAS");
                startActivity(intent);
            }
        });

        cardView=view.findViewById(R.id.PagosProgramados_Card);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),Ayuda_Home.class);
                intent.putExtra("CASO","PAGOSPROGRAMADOS");
                startActivity(intent);
            }
        });

        cardView=view.findViewById(R.id.Deudas_Card);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),Ayuda_Home.class);
                intent.putExtra("CASO","DEUDAS");
                startActivity(intent);
            }
        });

        cardView=view.findViewById(R.id.Objetivos_Card);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),Ayuda_Home.class);
                intent.putExtra("CASO","OBJETIVOS");
                startActivity(intent);
            }
        });

        cardView=view.findViewById(R.id.Notificaciones_Card);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getContext(),Ayuda_Home.class);
                intent.putExtra("CASO","NOTIFICACIONES");
                startActivity(intent);
            }
        });

        return view;
    }
}