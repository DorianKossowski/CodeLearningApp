export const task = `
-> statement
    with text: "boolean greater = a > 2"
    log info: "Definicja zmiennej greater"
-> statement
    with text: "boolean lesser = a < 4"
    log info: "Definicja zmiennej lesser"
-> statement
    with resolved: "System.out.println(\\"a jest równe 0\\")"
    with else if: "a == 0"
    log info: "Wywołanie metody println wewnątrz else if"
`;

export const input = `public class MyClass {
    public static void main(String[] args){
        int a = ;

        boolean greater = a > 2;
        boolean lesser = a < 4;

        if(greater && lesser) {
            System.out.println(a);
        } else {
            System.out.println("Nie jest z zakresu od 2 do 4. Nie jest też 0.");
        }
    }
}`;

export const description = `Java umożliwia również łączenie słów if oraz else. Dzięki takiemu mechanizmowi możliwe jest sprawdzenie kilku warunków w ramach jednej instrukcji if. W takim wypadku wykonane zostaną instrukcje z pierwszego przypadku, dla którego warunek jest spełniony.
<java>
if(warunek1) {
	ciało if - gdy warunek1 prawdziwy
} else if(warunek2) {
    ciało else if - gdy warunek1 fałszywy i warunek2 prawdziwy
}
</java>`;

export const instruction = `Uzupełnij widoczny kod o brakujące elementy, tak aby przy użyciu konstrukcji else if wypisany na ekran (println) został napis: "a jest równe 0" i był on zgodny ze zdefiniowanym warunkiem.`;

export const hasNext = true;