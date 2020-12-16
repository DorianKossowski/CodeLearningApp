export const task = `
-> method
    with modifiers: { "public", "static" }
    with result: "void"
    with name: "main"
    with args: { "String[]", - }
-> statement
    in method: "main"
    with text: "System.out.println(\\"My Hello World\\")"
    log info: "Wywołanie metody z wykorzystaniem literału"
`;

export const input = `public class Hello {
    public void (String[] args){
        System.out.println();
    }
}`;

export const description = `Jak wspomniane zostało w poprzednim ćwiczeniu, od metody <b>main</b> ropoczyna się działanie programu. W jej wnętrzu możliwe jest używanie wszelakich instrukcji jak np. wywołanie metody <b>println</b>.
Metoda startowa <b>main</b> charakteryzuje się zwracanym typem <b>void</b> oraz znajdującym się w nawiasach argumentem w postaci tablicy łańcuchów znaków - <b>String[]</b>. Wymagane są również odpowiednie modyfikatory - <b>public</b> oraz <b>static</b>.`;

export const instruction = `Uzupełnij definicję metody uruchomieniowej main o jej brakujące elementy.
Korzystając z literału znakowego wyświetl napis "My Hello World".`;

export const hasNext = true;