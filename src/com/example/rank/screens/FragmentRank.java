package com.example.rank.screens;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
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
 * Time: 10:04
 */
public class FragmentRank extends Fragment {

    private View view;
    private Round selectedRound;
    private List<ViewUserRank> viewUserRanks;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.consult_rank, container, false);
        initialize();
        createViews(getOrdenadedValues());
        return view;
    }

    private List<UserValue> getOrdenadedValues() {
        List<UserValue> values = new ArrayList<>();
        for (Usuario usuario : Persistence.getInstance().getUsersByRound(selectedRound.getId())) {
            double userBalance = Persistence.getInstance().getUserBalanceByRound((int) usuario.getId(), (int) selectedRound.getId());
            UserValue userValue = new UserValue(usuario.getNome(), userBalance);
            values.add(userValue);
        }
        Collections.sort(values);
        return values;
    }

    private void initialize() {
        viewUserRanks = new ArrayList<>();
        selectedRound = Persistence.getInstance().getRoundById(((Main) getActivity()).getSelectedRound());
//        ((TextView) view.findViewById(R.id.text_view_titulo_rank)).setText(selectedRound.getId() + getActivity().getString(R.string.text_first_tourn));
//        ((TextView) view.findViewById(R.id.first_place_prize)).setText(Formatador.formatarDouble(selectedRound.getFirstPlaceValue()));
//        ((TextView) view.findViewById(R.id.second_place_prize)).setText(Formatador.formatarDouble(selectedRound.getSecondPlaceValue()));
    }

    private void createViews(List<UserValue> userValues) {
        LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.container_user_position);
        for (UserValue userValue : userValues) {
            ViewUserRank viewUserRank = new ViewUserRank(getActivity());
            viewUserRank.setRankType(ViewUserRank.Type.TYPE_ROUND);
            viewUserRank.setUserName(userValue.getName());
            viewUserRank.setUserValue(userValue.getValor());
            viewUserRank.setPositionNumber(userValues.indexOf(userValue) + 1);
            linearLayout.addView(viewUserRank);
            viewUserRanks.add(viewUserRank);
        }
    }

    private void updateViews(List<UserValue> userValues) {
        for (int i = 0; i < userValues.size(); i++) {
            viewUserRanks.get(i).setPositionNumber(i + 1);
            viewUserRanks.get(i).setUserName(userValues.get(i).getName());
            viewUserRanks.get(i).setUserValue(userValues.get(i).getValor());
        }
    }

    public void atualizar() {
        if (Persistence.getInstance().getLancamentosByRoundUser((int) selectedRound.getId()).size() > 0) {
            updateViews(getOrdenadedValues());
        }
    }

}