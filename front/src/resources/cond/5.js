export const task = `
-> statement
    with text: "int a = 0"
    log info: "Definicja zmiennej a"
-> statement
    with resolved: "System.out.println(\\"a == 0\\")"
    with switch expression: "a"
    with switch label: "0"
    log info: "Wywołanie metody println wewnątrz case"
`;

export const input = `public class MyClass {
    public static void main(String[] args){
        int a = 0;

    }
}`;

export const description = `W przypadku gdy mamy do czynienia z wieloma instrukcjami else if, zazwyczaj wygodniejsze będzie skorzystanie z instrukcji <b>switch</b>. Jest to druga z instrukcji warunkowych dostępnych w języku Java. Wyrażenie <b>switch</b> musi być typu znakowego, całkowitego lub String i jest ono porównywane z wartościami kolejnych elementów <b>case</b>. Jeżeli porównywane wartości się zgadzają, wykonywany zostaje kod z danego case'a. 
<java>
switch(wyrażenie) {
  case x:
    // blok kodu
    break;
  case y:
    // blok kodu
    break;
}
</java>
Należy zwrócić uwagę na słowo kluczowe <b>break</b>, która powoduje przerwanie wykonywania instrukcji <b>switch</b>. Jest ono opcjonalne, a jego brak powoduje wykonanie także wszystkich instrukcji znajdujących się w poniższych case'ach, bez względu na to, czy zgadzają się z wyrażeniem.`;

export const instruction = `Dopisz odpowiednią instrukcję switch, tak aby napis "a == 0" został wypisany (println) z wewnątrz elementu case.`;

export const hasNext = true;