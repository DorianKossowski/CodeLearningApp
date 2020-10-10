export const task = `
-> method
    with modifiers: { "public", "static" }
    with result: "void"
    with name: "main"
    with args: { "String[]", - }
-> statement
    in method: "main"
    with text: "System.out.println(\\"Hello World\\")"
    error message: "Wywołanie metody z wykorzystaniem literału"
`;

export const input = `public class Hello {
    public static void main(String[] args){
        System.out.println("Hello World");
    }
}`;

export const description = `Przyjęło się, że przygodę z programowaniem rozpoczyna się od programu "Hello World". Jest to prosty kawałek kodu, który wyświetla na ekranie taki tekst.
W celu stworzenia w języku Java tego typu programu, należy rozpocząć od dodania odpowiedniej metody main. Jest to swego rodzaju punkt startowy, który pozwoli na uruchomienie programu.
Wewnątrz tej metody, należy następnie skorzystać z funkcji println, która to pozwala wyświetlić dowolny ciąg znaków.`;

export const instruction = `Przyjrzyj się i uruchom widoczny w edytorze kod. Zawiera on instrukcje niezbędne do poprawnego działania programu "Hello World".`;

export const hasNext = true;