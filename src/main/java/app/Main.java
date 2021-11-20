package app;

import app.db.Connector;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import static spark.Spark.*;

@Slf4j
public class Main {

    @SneakyThrows
    public static void main(String[] args) {
        port(Integer.parseInt(System.getProperty("PORT")));
        Password.init();
        var connector = new Connector();
        path("/api", () -> {
            post("/create", (req, res) -> connector.create());
            post("/delete/:api", (req, res) -> connector.delete(req.params(":api")));
            post("/renew/:api", (req, res) -> connector.renew(req.params(":api")));

            path("/:api", () -> {
                post("/add/:email", (req, res) -> connector.add(req.params(":api"), req.params(":email")));
                post("/remove/:email", (req, res) -> connector.remove(req.params(":api"), req.params(":email")));
                post("/list", (req, res) -> connector.list(req.params(":api")));

                path("/mailer", () -> {
                    post("", (req, res) -> connector.mailer(req.params(":api")));
                    post("/:username/:password", (req, res) -> connector.mailer(req.params(":api"), req.params(":username"), req.params(":password")));
                });
                post("/send/:title/:letter", (req, res) -> connector.send(req.params(":api"), req.params(":title"), req.params(":letter")));
            });
        });
    }
}
