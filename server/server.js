var express = require("express");
var bodyParser = require('body-parser');
var mysql = require('mysql');

var pool = mysql.createPool({
  connectionLimit: 50,
  host:'localhost',
  user:'junction',
  password:'2015',
  database:'junction',
});

app.post('/newuser', function(req, res){
  pool.getConnection(function(err, connection) {
    if(err){
      res.status(500).send("Cannot currently access database. Try again in couple minutes");
      return;
    } 

    var sql = "INSERT INTO users (account, location, interests) VALUES (" + connection.escape(req.body.account) + "," +
                                                                            connection.escape(req.body.location) + "," +
                                                                            connection.escape(req.body.interests) + ")";
    connection.query(sql, function(err, results, fields){
      if(err){
        res.status(500).send("Invalid database query. Check fields and try again.");
        return;
      }
      connection.release();
      res.status(200).send("Account created succesfully!");
    });
  });
});