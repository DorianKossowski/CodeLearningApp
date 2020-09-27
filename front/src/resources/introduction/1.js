export const task = `-> method
with modifiers: { "public", "static" }
with result: "void"
with name: "main"
with args: { "String[]", - }
-> statement
in method: "main"
with text: "System.out.print(\\"Hello World\\");"
error message: "Wywołanie metody z literału"`;

export const input = `public class Hello {
    public static void main(String[] args){
        System.out.print("Hello World");
    }
}`;

export const description = `Przyjęło się, że pierwszy program jaki tworzy się w dowolnym języku to zawsze "Hello World", czyli "Witaj Świecie". Jest to prosty program który wyświetla na ekranie taki tekst.
By tego dokonać należy najpierw dodać tak zwaną metodę main którą można uruchomić, czyli coś w rodzaju punktu startowego naszego programu.
Teraz możemy skorzystać z funkcji println, która to wyświetla dowolny ciąg znaków na tak zwanym standardowym wyjściu.`;