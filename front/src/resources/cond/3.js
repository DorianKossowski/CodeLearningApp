export const task = `
-> statement
    with text: "boolean greater = a > 2"
    log info: "Definicja zmiennej greater"
-> statement
    with text: "boolean lesser = a < 4"
    log info: "Definicja zmiennej lesser"
-> statement
    with resolved: "System.out.println(\\"Nie jest z zakresu od 2 do 4\\")"
    is in else
    log info: "Wywołanie metody println wewnątrz else"
`;

export const input = `public class MyClass {
    public static void main(String[] args){
        int a = ;

        boolean greater = a > 2;
        boolean lesser = a < 4;

        if(greater && lesser) {
            System.out.println(a);
        }
    }
}`;

export const description = `W momencie gdy konieczne jest wywołanie określonej logiki tylko gdy warunek if'a jest fałszywy, używa się słowa kluczowego <b>else</b>. Instrukcje do wywołania umieszcza się w jego ciele, analogicznie jak w przypadku if'a.
<java>
if(warunek) {
	ciało if - gdy warunek prawdziwy
} else {
    ciało else - gdy warunek fałszywy
}
</java>
Należy mieć na uwadze, że element <b>else</b> musi się znajdować zawsze na końcu instrukcji if.`;

export const instruction = `Uzupełnij widoczny kod o brakujące elementy, tak aby przy użyciu słowa kluczowego else wypisany na ekran (println) został napis: "Nie jest z zakresu od 2 do 4".`;

export const hasNext = true;