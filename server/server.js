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

  app.post('/changelocation', function(req, res){
    if(err){
      res.status(500).send("Cannot currently access database. Try again in couple minutes");
      return;
    }

    var sql = "UPDATE users SET location=" + connection.escape(req.body.location) + " WHERE account=" + connection.escape(req.body.account);
    connection.query(sql, function(err, results, fields){
      if(err){
        res.status(500).send("Invalid database query. Check fields and try again.");
        return;
      }
      connection.release();
      res.status(200).send("Location changed succesfully");
    });
  });
  
  app.post('/changeinterests', function(req, res){
    pool.getConnection(function(err, connection){
      if(err){
        res.status(500).send("Cannot currently access database. Try again in couple minutes");
        return;
      }
      
      var sql = "UPDATE users SET interests=" + connection.escape(req.body.interests) + " WHERE account =" + connection.escape(req.body.account);
      connection.query(sql, function(err, results, fields) {
        if(err){
          res.status(500).send("Invalid database query. Check fields and try again.");
          return;
        }
        connection.release();
        res.status(200).send("Interests changed succesfully");
      });
    });
  });

  app.post('/createEvent', function(req, res){
    pool.getConnection(function(err, connection) {
    if(err){
      res.status(500).send("Cannot currently access database. Try again in couple minutes");
      return;
    } 

    var sql = "INSERT INTO events (location, time, type) VALUES (" + connection.escape(req.body.location) + "," +
                                                                    connection.escape(req.body.time) + "," + 
                                                                    connection.escape(req.body.type) + ")";                                                                           connection.escape(req.body.interests) + ")";
    connection.query(sql, function(err, results, fields){
      if(err){
        res.status(500).send("Invalid database query. Check fields and try again.");
        return;
      }
      connection.release();
      res.status(200).send("Event created succesfully!");
    });
  });

  app.get('/events', function(req, res){
    pool.getConnection(function(err, connection){
      if(err){
        res.status(500).send("Cannot currently access database. Try again in couple minutes");
        return;
      }
      
      connection.query("SELECT * FROM events", function(err, results, fields) {
        if(err){
          res.status(500).send("Invalid database query. Check fields and try again.");
          return;
        }
        
        var sports = results;

        }

        var sql = "SELECT * FROM users WHERE account = " + connection.escape(req.body.account);
      
        connection.query(sql, function(err, results, fields) {
          if(err){
            res.status(500).send("Invalid database query. Check fields and try again.");
            return;
          }
          var userlocation = results[0].location;
          var possibleSports = [];

          for(var i = 0; i < results.length; i++){
            if(results[i].location * results[i].location + userlocation * userlocation < range * range){
              possibleSports.push(reuslts[i]);
            }
          }
          connection.release();
          res.status(200).send(possibleSports);
        });
      });
    });
  });
});