package com.example.rank.screens;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.example.rank.Formatador;
import com.example.rank.Main;
import com.example.rank.R;
import com.example.rank.component.ViewUserRank;
import com.example.rank.db.Persistence;
import com.example.rank.model.Round;
import com.example.rank.model.UserValue;
import com.example.rank.model.Usuario;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Daniel Ulrik
 * Date: 06/07/2015
 * Time: 10:03
 */
public class FragmentPeriod extends Fragment {

    private View view;
    private TextView textViewDataIni;
    private TextView textViewDataFim;
    private List<ViewUserRank> viewUserRanks;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.add_new_result, container, false);
        Round selectedRound = Persistence.getInstance().getRoundById(((Main) getActivity()).getSelectedRound());
        initialize();
        if (selectedRound != null) {
            textViewDataIni.setText(Formatador.formatarData(selectedRound.getIniDate()));
            textViewDataFim.setText(Formatador.formatarData(selectedRound.getEndDate()));
        }

//        if (Persistence.getInstance().isRoundFinished((int) selectedRound.getId(), selectedRound.getIniDate(), selectedRound.getEndDate())) {
//            Usuario u = Persistence.getInstance().getRoundWinner((int) selectedRound.getId());
//            ((TextView) view.findViewById(R.id.text_view_wier)).setText(u.getNome());
//        }

        fillViewContainer();
        return view;
    }

    private void initialize() {
        viewUserRanks = new ArrayList<>();
        textViewDataIni = (TextView) view.findViewById(R.id.text_view_data_ini);
        textViewDataFim = (TextView) view.findViewById(R.id.text_view_data_fim);
    }

    private void fillViewContainer() {
        LinearLayout layoutContainerDados = (LinearLayout) view.findViewById(R.id.layout_container_dados_gerais);
        List<Usuario> usersInRound = Persistence.getInstance().getUsersByRound(((Main) getActivity()).getSelectedRound());
        List<UserValue> userValues = new ArrayList<>();
        for (Usuario user : usersInRound) {
            ViewUserRank viewUserRank = new ViewUserRank(getActivity());
            viewUserRank.setRankType(ViewUserRank.Type.TYPE_ALL);
            layoutContainerDados.addView(viewUserRank);
            viewUserRanks.add(viewUserRank);
            userValues.add(new UserValue(user.getNome(), Persistence.getInstance().getTotalValueByUser(user.getId())));
        }
        Collections.sort(userValues);
        updateTotalUsers(userValues);
    }

    private void updateTotalUsers(List<UserValue> userValues) {
        for (int i = 0; i < userValues.size(); i++) {
            viewUserRanks.get(i).setUserName(userValues.get(i).getName());
            viewUserRanks.get(i).setUserValue(userValues.get(i).getValor());
            viewUserRanks.get(i).setPositionNumber(i + 1);
        }
    }
}