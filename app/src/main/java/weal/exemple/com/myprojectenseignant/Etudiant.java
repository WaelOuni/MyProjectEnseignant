package weal.exemple.com.myprojectenseignant;

/**
 * Created by WIN on 31/05/2015.
 */
public class Etudiant {
    int cin, inscription;
    String nom,prenom, genre, password, email, niveau, telephone;
    public Etudiant (int ecin, String enom,String eprenom,String egenre ,String eemail,String eniveau, int einscription,String etelephone){
        cin=ecin;
        nom=enom;
        prenom=eprenom;
        genre=egenre;
        email=eemail;
        niveau=eniveau;
        inscription= einscription;
        telephone=etelephone;
    }
}
