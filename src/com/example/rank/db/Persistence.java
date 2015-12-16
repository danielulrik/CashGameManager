package com.example.rank.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.rank.DataUtil;
import com.example.rank.model.Lancamento;
import com.example.rank.model.Round;
import com.example.rank.model.Usuario;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Daniel Ulrik
 * Date: 06/07/2015
 * Time: 11:29
 */
public class Persistence {
    private static Persistence instance;
    private SQLiteDatabase db;
    private DBHandler dbHandler;

    private Persistence() {
    }

    public static Persistence getInstance() {
        if (instance == null) {
            instance = new Persistence();
        }
        return instance;
    }

    public void setContexDatabase(Context context) {
        dbHandler = new DBHandler(context);
        instance.db = dbHandler.getWritableDatabase();
    }

    public void createTables() {
        dbHandler.onCreate(db);
    }

    //usuarios

    public int getNumberRoundsByUser(Usuario usuario) {
        return -1;
    }

    public List<Usuario> getAllUsuarios() {
        List<Usuario> usuarios = new LinkedList<>();
        Cursor cursor = db.query("usuario", new String[]{"_id, nome"}, null, null, null, null, "nome");
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                Usuario usuario = new Usuario();
                usuario.setId(cursor.getLong(0));
                usuario.setNome(cursor.getString(1));
                usuarios.add(usuario);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return usuarios;
    }

    public boolean containsUser(Usuario user) {
        Cursor cursor = db.rawQuery("select * from usuario where nome = ?", new String[]{user.getNome()});
        if (cursor.getCount() > 0) {
            cursor.close();
            return true;
        }
        return false;
    }

    public List<Usuario> getUsersByRound(long idRound) {
        List<Usuario> users = new LinkedList<>();
        Cursor cursor = db.rawQuery("select idUsuario from usuarioround where idRound = ?", new String[]{idRound + ""});
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                users.add(getUsuarioById(cursor.getInt(0)));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return users;
    }

    public Usuario getUsuarioById(int id) {
        Usuario usuario = null;
        Cursor cursor = db.rawQuery("SELECT * FROM usuario where _id = ?", new String[]{id + ""});
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            usuario = new Usuario(cursor.getLong(0), cursor.getString(1));
        }
        cursor.close();
        return usuario;
    }

    public Usuario getUsuarioByName(String name) {
        Usuario usuario = null;
        Cursor cursor = db.rawQuery("select * from usuario where nome = ?", new String[]{name});
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            usuario = new Usuario(cursor.getLong(0), cursor.getString(1));
        }
        cursor.close();
        return usuario;
    }

    //lancamentos

    public List<Lancamento> getLancamentosByRoundUser(int id) {
        List<Lancamento> lancamentos = new LinkedList<>();
        Cursor cursor = db.rawQuery("select * from lancamento where idRound = ? order by valor", new String[]{id + ""});
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                Lancamento lancamento = new Lancamento(cursor.getInt(0),
                        new Date(cursor.getLong(1)),
                        cursor.getDouble(2),
                        cursor.getDouble(3),
                        cursor.getDouble(4),
                        getUsuarioById(cursor.getInt(5)),
                        getRoundById(cursor.getInt(6)));

                lancamentos.add(lancamento);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return lancamentos;
    }

    private List<Lancamento> getLancamentosByRoundUser(int idUser, int idRound) {
        List<Lancamento> lancamentos = new LinkedList<>();
        String query = "select * from lancamento where idUsuario = ? and idRound = ?";
        String[] params = new String[]{"" + idUser, "" + idRound};
        Cursor cursor = db.rawQuery(query, params);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                Lancamento lancamento = new Lancamento(cursor.getInt(0),
                        new Date(cursor.getLong(1)),
                        cursor.getDouble(2),
                        cursor.getDouble(3),
                        cursor.getDouble(4),
                        getUsuarioById(cursor.getInt(5)),
                        getRoundById(cursor.getInt(6)));

                lancamentos.add(lancamento);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return lancamentos;
    }

    public List<Lancamento> getLancamentosByRound(int idRound) {
        List<Lancamento> lancamentos = new ArrayList<>();
        String query = "select * from lancamento where idRound = ?";
        String[] params = new String[]{"" + idRound};
        Cursor cursor = db.rawQuery(query, params);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                Lancamento lancamento = new Lancamento(cursor.getInt(0),
                        new Date(cursor.getLong(1)),
                        cursor.getDouble(2),
                        cursor.getDouble(3),
                        cursor.getDouble(4),
                        getUsuarioById(cursor.getInt(5)),
                        getRoundById(cursor.getInt(6)));

                lancamentos.add(lancamento);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return lancamentos;
    }

    private void deleteLancamentosFromRound(long id) {
        if (getRoundById((int) id) != null) {
            db.delete("lancamento", "idRound = ?", new String[]{"" + id});
        }
    }

    public Double getUserBalanceByRound(int idUser, int idRound) {
        Double value = 0.0;
        String query = "select SUM(valor) from lancamento where idUsuario = ? and idRound = ?";
        String[] params = new String[]{"" + idUser, "" + idRound};
        Cursor cursor = db.rawQuery(query, params);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            value = cursor.getDouble(0);
        }
        cursor.close();
        return value;
    }

    public Double getInValue(int idUser, int idRound, Date date) {
        List<Lancamento> lancamentos = getLancamentosByRoundUser(idUser, idRound);
        if (lancamentos.size() > 0) {
            for (Lancamento lancamento : lancamentos) {
                if (DataUtil.datasIguais(lancamento.getData(), date)) {
                    return lancamento.getIn();
                }
            }
        }
        return 0.0;
    }

    public Double getOutValue(int idUser, int idRound, Date date) {
        List<Lancamento> lancamentos = getLancamentosByRoundUser(idUser, idRound);
        if (lancamentos.size() > 0) {
            for (Lancamento lancamento : lancamentos) {
                if (DataUtil.datasIguais(lancamento.getData(), date)) {
                    return lancamento.getOut();
                }
            }
        }
        return 0.0;
    }

    public Double getTotalValueByUser(long idUser) {
        Double totalValue = 0.0;
        Cursor cursor = db.rawQuery("select sum(valor) from lancamento where idUsuario = ?", new String[]{"" + idUser});
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            totalValue = cursor.getDouble(0);
        }
        cursor.close();
        return totalValue;
    }

    public Usuario getRoundWinner(int idRound) {
        Usuario usuario = null;
        Cursor cursor = db.rawQuery("select idUsuario, sum(valor) as total from lancamento where idRound = ? order by total desc", new String[]{"" + idRound});
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            usuario = getUsuarioById((int) cursor.getLong(0));
        }
        cursor.close();
        return usuario;
    }

    public boolean isRoundFinished(int idRound, Date ini, Date end) {
        boolean finished = true;
        for (Date date : DataUtil.getDatasEntreDatas(ini, end)) {
            if (!containsBalance(idRound, date)) {
                finished = false;
                break;
            }
        }
        return finished;
    }

    //rounds

    public List<Round> getAllRounds() {
        List<Round> rounds = new LinkedList<>();
        Cursor cursor = db.rawQuery("select * from round order by _id DESC", null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                Round round = new Round();
                round.setId(cursor.getLong(0));
                round.setIniDate(new Date(cursor.getLong(1)));
                round.setEndDate(new Date(cursor.getLong(2)));
                round.setTornBuyIn(cursor.getDouble(3));
                round.setUsers(getUsersByRound(cursor.getLong(0)));
                rounds.add(round);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return rounds;
    }

    public Round getLastRound() {
        Round round = null;
        Cursor cursor = db.rawQuery("SELECT * FROM round ORDER BY _id DESC LIMIT 1", null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            round = new Round(cursor.getLong(0), new Date(cursor.getLong(1)), new Date(cursor.getLong(2)), cursor.getDouble(3));
        }
        cursor.close();
        return round;
    }

    public Round getRoundById(int id) {
        Round round = null;
        Cursor cursor = db.rawQuery("SELECT * FROM round where _id = ?", new String[]{id + ""});
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            round = new Round(cursor.getLong(0), new Date(cursor.getLong(1)), new Date(cursor.getLong(2)), cursor.getDouble(3), getUsersByRound(id));
        }
        cursor.close();
        return round;
    }

    public boolean containsBalance(int idRound, Date date) {
        List<Lancamento> lancamentos = getLancamentosByRound(idRound);
        List<Lancamento> lancamentosDia = new LinkedList<>();
        if (lancamentos.size() > 0) {
            for (Lancamento l : lancamentos) {
                if (DataUtil.datasIguais(l.getData(), date)) {
                    lancamentosDia.add(l);
                    break;
                }
            }
        }
        return !lancamentosDia.isEmpty();
    }

    // imports

    public void handleImportedEntities(Round round, List<Usuario> usuarios, List<Lancamento> lancamentos) {
        List<Usuario> postUsers = handleImportedUsers(usuarios);
        round.setUsers(postUsers);
        Round postRound = handleImportedRound(round);
        for (Lancamento lancamento : lancamentos) {
            lancamento.setRound(postRound);
            lancamento.setUsuario(getUsuarioByName(lancamento.getUsuario().getNome()));
        }
        deleteLancamentosFromRound(postRound.getId());
        for (Lancamento lancamento : lancamentos) {
            insertDayBalance(lancamento);
        }
    }

    private Round handleImportedRound(Round round) {
        boolean insert = true;
        List<Round> previousRounds = getAllRounds();
        for (Round previousRound : previousRounds) {
            if (DataUtil.datasIguais(previousRound.getIniDate(), round.getIniDate())
                    && DataUtil.datasIguais(previousRound.getEndDate(), round.getEndDate())) {
                if (sameUsersInBothLists(previousRound.getUsers(), round.getUsers())) {
                    insert = false;
                    round = previousRound;
                    break;
                }
            }
        }
        if (insert) {
            long insertedId = createNewRound(round);
            round.setId(insertedId);
        }
        return round;
    }

    private boolean sameUsersInBothLists(List<Usuario> previousUsers, List<Usuario> importedUsers) {
        for (Usuario previousUser : previousUsers) {
            boolean containsUser = false;
            for (Usuario importedUser : importedUsers) {
                if (previousUser.getNome().equals(importedUser.getNome())) {
                    containsUser = true;
                    break;
                }
            }
            if (!containsUser) {
                return false;
            }
        }
        return true;
    }

    private List<Usuario> handleImportedUsers(List<Usuario> users) {
        List<Usuario> returnedUsers = new ArrayList<>();
        List<Usuario> previousUsers = getAllUsuarios();
        if (previousUsers.isEmpty()) {
            for (Usuario user : users) {
                saveUser(user);
            }
            returnedUsers = getAllUsuarios();
        } else {
            for (Usuario userJson : users) {
                boolean insert = true;
                for (Usuario previousUser : previousUsers) {
                    if (userJson.getNome().equalsIgnoreCase(previousUser.getNome())) {
                        insert = false;
                        returnedUsers.add(previousUser);
                    }
                }
                if (insert) {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("nome", userJson.getNome());
                    long id = db.insert("usuario", null, contentValues);
                    userJson.setId(id);
                    returnedUsers.add(userJson);
                }
            }
        }
        return returnedUsers;
    }

    // insert data

    public long saveUser(Usuario user) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("nome", user.getNome());
        return db.insert("usuario", null, contentValues);
    }

    public long createNewRound(Round round) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("dataini", round.getIniDate().getTime());
        contentValues.put("datafim", round.getEndDate().getTime());
        contentValues.put("taxa", round.getTornBuyIn());
        long insertedId = db.insert("round", null, contentValues);
        List<Usuario> users = round.getUsers();
        if (!users.isEmpty()) {
            ContentValues values = new ContentValues();
            for (Usuario user : users) {
                values.put("idUsuario", user.getId());
                values.put("idRound", insertedId);
                db.insert("usuarioround", null, values);
            }
        }
        return insertedId;
    }

    public void insertDayBalance(Lancamento lancamento) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("data", lancamento.getData().getTime());
        contentValues.put("valor", lancamento.getValor());
        contentValues.put("buyin", lancamento.getIn());
        contentValues.put("out", lancamento.getOut());
        contentValues.put("idUsuario", lancamento.getUsuario().getId());
        contentValues.put("idRound", lancamento.getRound().getId());

        long insertedId;

        if ((insertedId = containsLancamento(lancamento)) == -1) {
            db.insert("lancamento", null, contentValues);
        } else {
            db.update("lancamento", contentValues, "_id = ?", new String[]{insertedId + ""});
        }
    }

    private long containsLancamento(Lancamento lancamento) {
        List<Lancamento> lancamentos = getLancamentosByRoundUser((int) lancamento.getUsuario().getId(), (int) lancamento.getRound().getId());
        if (!lancamentos.isEmpty()) {
            for (Lancamento lancSalvo : lancamentos) {
                if (DataUtil.datasIguais(lancamento.getData(), lancSalvo.getData())) {
                    return lancSalvo.getId();
                }
            }
        }
        return -1L;
    }
}
