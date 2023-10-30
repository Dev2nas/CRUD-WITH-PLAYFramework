package controllers;
import Models.Personne;
import play.data.FormFactory;
import play.mvc.*;
import services.PersonneService;

import javax.inject.Inject;
import java.sql.SQLException;
import java.util.List;

public class crudController extends Controller {

    private final PersonneService personneService;
    private final FormFactory formFactory;

    @Inject
    public crudController(FormFactory formFactory, PersonneService personneService) {
        this.formFactory = formFactory;
        this.personneService = personneService;
    }

    // Affiche le formulaire d'ajout d'une nouvelle personne
    public Result FormAdd(Http.Request request) throws SQLException, ClassNotFoundException {
        return ok(views.html.index.render(request));
    }

    // Affiche le formulaire de modification d'une personne existante
    public Result FormModif(Http.Request request, int id) throws SQLException, ClassNotFoundException {
        return ok(views.html.fromUpdate.render(personneService.findByID(id), request));
    }

    // Traite la soumission du formulaire d'ajout d'une nouvelle personne
    public Result add(Http.Request request) throws SQLException, ClassNotFoundException {
        // Récupère les données du formulaire
        String nom = formFactory.form().bindFromRequest(request).get("nom");
        String prenom = formFactory.form().bindFromRequest(request).get("prenom");
        int age = Integer.parseInt(formFactory.form().bindFromRequest(request).get("age"));
        char sexe = formFactory.form().bindFromRequest(request).get("sexe").charAt(0);
        String telephone = formFactory.form().bindFromRequest(request).get("telephone");
        String email = formFactory.form().bindFromRequest(request).get("email");
        String profession = formFactory.form().bindFromRequest(request).get("profession");

        // Crée un objet Personne et le remplit avec les données du formulaire
        Personne personne = new Personne();
        personne.setNom(nom);
        personne.setPrenom(prenom);
        personne.setAge(age);
        personne.setSexe(sexe);
        personne.setEmail(email);
        personne.setTelephone(telephone);
        personne.setProfession(profession);

        // Ajoute la personne à la base de données
        personneService.addPersonne(personne);

        // Redirige vers la liste des personnes
        return redirect("personnes");
    }

    // Traite la mise à jour des informations d'une personne
    public Result update(Http.Request request) throws ClassNotFoundException, SQLException{
        // Récupère les données du formulaire de mise à jour
        int id = Integer.parseInt(formFactory.form().bindFromRequest(request).get("id"));
        String nom = formFactory.form().bindFromRequest(request).get("nom");
        String prenom = formFactory.form().bindFromRequest(request).get("prenom");
        int age = Integer.parseInt(formFactory.form().bindFromRequest(request).get("age"));
        char sexe = formFactory.form().bindFromRequest(request).get("sexe").charAt(0);
        String telephone = formFactory.form().bindFromRequest(request).get("telephone");
        String email = formFactory.form().bindFromRequest(request).get("email");
        String profession = formFactory.form().bindFromRequest(request).get("profession");

        // Crée un objet Personne et le remplit avec les données du formulaire
        Personne personne = new Personne();
        personne.setId(id);
        personne.setNom(nom);
        personne.setPrenom(prenom);
        personne.setAge(age);
        personne.setSexe(sexe);
        personne.setEmail(email);
        personne.setTelephone(telephone);
        personne.setProfession(profession);
        if(personneService.update(personne).equals("modifier"))
            System.out.println("Modification du personnel :"+personne.getNom()+" "+personne.getPrenom()+" "+personne.getSexe()+" "+personne.getTelephone()+" "+personne.getProfession()+" "+personne.getEmail()+" "+personne.getAge()+" Son ID = "+personne.getId()+" C'est effectue avec sucess !!!!!!");
        else
            System.err.println("Modification non effectuer" + personneService.update(personne));

        // Redirige vers la liste des personnes
        return redirect("personnes");

    }

    // Traite la suppression d'une personne
    public Result delete(Http.Request request, int id) throws ClassNotFoundException, SQLException {
        // Appelle la méthode de suppression dans le service
        personneService.delete(id);

        // Redirige vers la liste des personnes
        return redirect("personnes");
    }

    // Affiche la liste des personnes
    public Result listePersonnes(Http.Request request) throws SQLException, ClassNotFoundException {
        return ok(views.html.ListPersonne.render(personneService.ListePersonne(), request));
    }

    // Traite la recherche de personnes
    public Result rechercher(Http.Request request) throws SQLException, ClassNotFoundException {
        // Récupère le terme de recherche à partir du formulaire
        String mot = formFactory.form().bindFromRequest(request).get("recherche");

        // Effectue la recherche dans la base de données
        List<Personne> personnes = personneService.rechercher(mot);

        // Affiche les résultats dans la vue de recherche
        return ok(views.html.rechercherPersonne.render(personnes, request));
    }

    // Autre méthode pour afficher la liste des personnes (peut être supprimée si elle n'est pas utilisée)
    public Result rechecherPersonne(Http.Request request) throws SQLException, ClassNotFoundException {
        return ok(views.html.rechercherPersonne.render(personneService.ListePersonne(), request));
    }
}