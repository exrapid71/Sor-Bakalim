package controllers;

import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import views.html.index;

public class HomeController extends Controller {


    public Result index() {
        Http.Context ctx = ctx();
        return ok(index.render("Your new application is ready."));
    }

}
