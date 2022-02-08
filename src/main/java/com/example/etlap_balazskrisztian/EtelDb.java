package com.example.etlap_balazskrisztian;

import java.sql.*;
        import java.util.ArrayList;
        import java.util.List;

public class EtelDb {
    Connection conn;

    public EtelDb() throws SQLException {
        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/etlapdb", "root", "");
    }

    public List<Kategoria> getKategoria() throws SQLException {
        List<Kategoria> kategoriaList = new ArrayList<>();
        String sql = "SELECT * FROM kategoria ORDER BY id";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        while(rs.next()) {
            kategoriaList.add(new Kategoria(
                    rs.getInt("id"),
                    rs.getString("Név")
            ));
        }

        return kategoriaList;
    }

    public List<Etel> getEtel() throws SQLException {
        List<Etel> etelList = new ArrayList<>();
        String sql = "SELECT etlap.id, etlap.nev, leiras, ar, kategoria.nev AS kategoria FROM `etlap` INNER JOIN kategoria ON etlap.kategoria_id = kategoria.id";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        while(rs.next()) {
            etelList.add(new Etel(
                    rs.getInt("id"),
                    rs.getString("Név"),
                    rs.getString("Leírás"),
                    rs.getInt("Ár"),
                    rs.getString("Kategória")
            ));
        }

        return etelList;
    }

    public int kategoriaHozzaad(String nev) throws SQLException {
        String sql = "INSERT INTO kategoria(nev) VALUES(?)";
        PreparedStatement stmt = conn.prepareStatement(sql);

        stmt.setString(1, nev);

        return stmt.executeUpdate();
    }

    public int kategoriaTorles(int id) throws SQLException {
        String sql = "DELETE FROM kategoria WHERE id = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);

        stmt.setInt(1, id);

        return stmt.executeUpdate();
    }

    public int etelHozzaad(String nev, String leiras, int ar, int kategoria) throws SQLException {
        String sql = "INSERT INTO etlap(nev, leiras, ar, kategoria_id) VALUES(?, ?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(sql);

        stmt.setString(1, nev);
        stmt.setString(2, leiras);
        stmt.setInt(3, ar);
        stmt.setInt(4, kategoria);

        return stmt.executeUpdate();
    }

    public int etelTorles(int id) throws SQLException {
        String sql = "DELETE FROM etlap WHERE id = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, id);
        return stmt.executeUpdate();
    }

    public int emelEgy(double ar, int id) throws SQLException {
        String sql;

        if (ar < 2) {
            sql = "UPDATE etlap SET ar = ar * ? WHERE id = ?";
        } else {
            sql = "UPDATE etlap SET ar = ar + ? WHERE id = ?";
        }

        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setDouble(1, ar);
        stmt.setInt(2, id);
        return stmt.executeUpdate();
    }

    public int emelMind(double ar) throws SQLException {
        String sql;

        if (ar < 2) {
            sql = "UPDATE etlap SET ar = ar * ?";
        } else {
            sql = "UPDATE etlap SET ar = ar + ?";
        }

        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setDouble(1, ar);
        return stmt.executeUpdate();
    }
}