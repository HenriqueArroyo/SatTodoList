public class Task {
//atributos
private String description;
private boolean done;
private String category; // Adicione uma propriedade para a categoria

//construtor(somente String description)
public Task(String description, String category) {
this.description = description;
this.done = false;
this.category = category; // Inicialize a categoria da tarefa
}
//set and Gets
public String getDescription() {
return description;
}
public boolean isDone() {
return done;
}
public void setDone(boolean done) {
this.done = done;
}
public String getCategory() {
    return category;
}
public void setCategory(String category) {
    this.category = category;
}


}