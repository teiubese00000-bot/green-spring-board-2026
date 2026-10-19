package green;

public class Book {

   String title;
    void clearTitle(){
        title="";
    }
    void changeTitle(String newTitle){
        title=newTitle;
    }
     String readTitle(){
         return title;
    }
}
