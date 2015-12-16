package com.example.rank.screens;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.rank.DataUtil;
import com.example.rank.Formatador;
import com.example.rank.Main;
import com.example.rank.R;
import com.example.rank.component.SimpleDatePickerDialog;
import com.example.rank.component.ViewUserDayBalance;
import com.example.rank.db.Persistence;
import com.example.rank.model.Lancamento;
import com.example.rank.model.Round;
import com.example.rank.model.Usuario;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created with Intellij IDEA.
 * User: Daniel Ulrik
 * Date: 20/07/2015
 * Time: 09:40
 */
public class FragmentAddNewBalance extends Fragment {

    public static final int IN_KEY = 333;
    public static final int OUT_KEY = 555;

    private View view;
    private Round selectedRound;
    private LinearLayout containerViewBalance;
    private List<ViewUserDayBalance> listViewsBalance;
    private Button buttonSave;
    private ViewUserDayBalance selectedUserView;
    private SimpleDatePickerDialog simpleDatePickerDialog;
    private TextView textViewDataCorrente;
    private Dialog datePickerDialog;
    private ImageButton imageButtonPickDate;
    private TextView textViewTotalIn;
    private TextView textViewTotalOut;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.add_new_balance, container, false);
        listViewsBalance = new ArrayList<>();
        selectedRound = Persistence.getInstance().getRoundById(((Main) getActivity()).getSelectedRound());
        initialize();
        imageButtonPickDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });
        simpleDatePickerDialog.setOnSelectedDate(new SimpleDatePickerDialog.OnSelectedDate() {
            @Override
            public void selectedDate(Date selectedDate) {
                if (selectedDate.before(selectedRound.getIniDate()) || selectedDate.after(selectedRound.getEndDate())) {
                    Formatador.showMsg(getActivity(), getString(R.string.msg_data_fora_periodo));
                } else {
                    simpleDatePickerDialog.updateDate(selectedDate);
                    textViewDataCorrente.setText(Formatador.formatarData(selectedDate));
                    loadValuesByDay(selectedDate);
                }
            }
        });

        textViewDataCorrente.setText(Formatador.formatarData(configInitialRoundDay()));
        loadValuesByDay(Formatador.getDateFromString(textViewDataCorrente.getText().toString()));
        setButtonSaveListener();
        return view;
    }

    private Date configInitialRoundDay() {
        Date today = new Date();
        Date returnDate = null;
        Date ini = selectedRound.getIniDate();
        Date end = selectedRound.getEndDate();
        if (!today.before(ini) && !today.after(end)) {
            List<Date> datas = DataUtil.getDatasEntreDatas(ini, end);
            for (int i = datas.size() - 1; i > -1; i--) {
                if (Persistence.getInstance().containsBalance((int) selectedRound.getId(), datas.get(i))) {
                    returnDate = datas.get(i);
                    break;
                }
            }
            if (returnDate == null) {
                returnDate = ini;
            }
        } else {
            returnDate = end;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(returnDate);
        simpleDatePickerDialog.updateDate(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        return returnDate;
    }

    private void initialize() {
        textViewTotalIn = (TextView) view.findViewById(R.id.text_view_total_in);
        textViewTotalOut = (TextView) view.findViewById(R.id.text_view_total_out);
        imageButtonPickDate = (ImageButton) view.findViewById(R.id.image_pick_date);
        simpleDatePickerDialog = new SimpleDatePickerDialog(getActivity());
        datePickerDialog = simpleDatePickerDialog.createDialog();
        containerViewBalance = (LinearLayout) view.findViewById(R.id.container_user_data);
        initializeViewsBalance();
        buttonSave = (Button) view.findViewById(R.id.button_salvar);
        textViewDataCorrente = (TextView) view.findViewById(R.id.text_view_data_corrente);
    }

    private void initializeViewsBalance() {
        List<Usuario> users = Persistence.getInstance().getUsersByRound(selectedRound.getId());
        for (Usuario usuario : users) {
            final ViewUserDayBalance viewUserDayBalance = new ViewUserDayBalance(getActivity());
            viewUserDayBalance.setUser(usuario);
            viewUserDayBalance.setListenerIn(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    treatListenerIn(viewUserDayBalance);
                }
            });
            viewUserDayBalance.setListenerOut(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    treatListenerOut(viewUserDayBalance);
                }
            });
            containerViewBalance.addView(viewUserDayBalance);
            listViewsBalance.add(viewUserDayBalance);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == -1) {
            if (selectedUserView != null) {
                if (data != null) {
                    double value = data.getDoubleExtra("typedValue", 0.0);
                    if (requestCode == IN_KEY) {
                        selectedUserView.setInValue(value);
                    } else if (requestCode == OUT_KEY) {
                        selectedUserView.setOutValue(value);
                    }
                }
            }
        }
        updateTotalDia();
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void treatListenerIn(ViewUserDayBalance selectedUserView) {
        this.selectedUserView = selectedUserView;
        Intent intent = new Intent(getActivity(), AddNewBalanceValue.class);
        intent.putExtra("SelectedInput", "in");
        startActivityForResult(intent, IN_KEY);
    }

    private void treatListenerOut(ViewUserDayBalance selectedUserView) {
        this.selectedUserView = selectedUserView;
        Intent intent = new Intent(getActivity(), AddNewBalanceValue.class);
        intent.putExtra("SelectedInput", "out");
        startActivityForResult(intent, OUT_KEY);
    }

    private void setButtonSaveListener() {
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserBalance();
                ((Main) getActivity()).setPositionViewPager(2);
            }
        });
    }

    private void saveUserBalance() {
        double inValues[] = new double[listViewsBalance.size()];
        double outValues[] = new double[listViewsBalance.size()];
        double balances[] = new double[listViewsBalance.size()];

        for (int i = 0; i < listViewsBalance.size(); i++) {
            inValues[i] = listViewsBalance.get(i).getInValue();
            outValues[i] = listViewsBalance.get(i).getOutValue();
            balances[i] = outValues[i] - inValues[i];
        }

        insertValues(balances, inValues, outValues);
        ((Main) getActivity()).updateViews();
    }

    private void insertValues(double[] values, double[] in, double[] out) {
        String strDate = ((TextView) view.findViewById(R.id.text_view_data_corrente)).getText().toString();
        Date date = null;
        try {
            date = new SimpleDateFormat(getString(R.string.date_format)).parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < values.length; i++) {
            Usuario user = listViewsBalance.get(i).getUser();
            Lancamento lancamento = new Lancamento(date, values[i], user, selectedRound, in[i], out[i]);
            Persistence.getInstance().insertDayBalance(lancamento);
        }
        loadValuesByDay(date);
        Formatador.showMsg(getActivity(), getActivity().getString(R.string.msg_salvo_sucesso));
    }

    private void updateViews(boolean enabled, String buttonText) {
        for (ViewUserDayBalance viewUserDayBalance : listViewsBalance) {
            viewUserDayBalance.setStateViews(enabled);
        }
        buttonSave.setTextColor(Color.WHITE);
        buttonSave.setText(buttonText);
        buttonSave.setEnabled(enabled);
        updateTotalDia();
    }

    private void loadValuesByDay(Date selectedDate) {
        double inValues[] = new double[listViewsBalance.size()];
        double outValues[] = new double[listViewsBalance.size()];
        for (int i = 0; i < listViewsBalance.size(); i++) {
            inValues[i] = Persistence.getInstance().getInValue((int) listViewsBalance.get(i).getUser().getId(), (int) selectedRound.getId(), selectedDate);
            outValues[i] = Persistence.getInstance().getOutValue((int) listViewsBalance.get(i).getUser().getId(), (int) selectedRound.getId(), selectedDate);
        }
        for (int j = 0; j < outValues.length; j++) {
            listViewsBalance.get(j).setInValue(inValues[j]);
            listViewsBalance.get(j).setOutValue(outValues[j]);
        }
        updateViews(true, getActivity().getString(R.string.text_lancar));
    }

    public void updateTotalDia() {
        double totalIn = 0.0;
        double totalOut = 0.0;
        for (ViewUserDayBalance viewUserDayBalance : listViewsBalance) {
            totalIn += viewUserDayBalance.getInValue();
            totalOut += viewUserDayBalance.getOutValue();
        }
        textViewTotalIn.setText(Formatador.formatarDouble(totalIn));
        textViewTotalOut.setText(Formatador.formatarDouble(totalOut));
    }

}