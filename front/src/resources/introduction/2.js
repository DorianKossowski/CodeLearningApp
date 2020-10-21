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

export const description = `Jak wspomniane zostało w poprzednim ćwiczeniu, od metody main ropoczyna się działanie programu. W jej wnętrzu możliwe jest używanie wszelakich instrukcji jak np. wywołanie metody println.
Metoda startowa main charakteryzuje się zwracanym typem void oraz znajdującym się w nawiasach argumentem w postaci tablicy łańcuchów znaków - String[]. Wymagane są również odpowiednie modyfikatory - public oraz static.`;

export const instruction = `Uzupełnij definicję metody uruchomieniowej main o jej brakujące elementy.
Korzystając z literału znakowego wyświetl napis "My Hello World".`;

export const hasNext = true;