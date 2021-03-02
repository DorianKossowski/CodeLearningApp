export const task = `
-> method
    with modifiers: { "static" }
    with name: "makeTriple"
    with result: "int"
-> statement
    with text: "System.out.println(makeTriple(i))"
    with resolved: "System.out.println(0)"
    in method: "main"
    with for iteration: "0"
    log info: "Wywołanie metody println w 0 iteracji"
-> statement
    with text: "System.out.println(makeTriple(i))"
    with resolved: "System.out.println(3)"
    in method: "main"
    with for iteration: "1"
    log info: "Wywołanie metody println w 1 iteracji"
-> statement
    with text: "System.out.println(makeTriple(i))"
    with resolved: "System.out.println(6)"
    in method: "main"
    with for iteration: "2"
    log info: "Wywołanie metody println w 2 iteracji"
`;

export const input = `public class MyClass {
    public static void main(String[] args){
        for(int i=0; i<3; i=i+1)
            System.out.println(makeTriple(i));
    }

}`;

export const description = `Często wydzielając logikę do osobnej metody, konieczne jest zrócenie rezultatu jej działania. Można tego dokonać używając instrukcji <b>return</b>. Pozwala ona na przerwanie, w dowolnym miejscu, działania metody i zwrócenie określonej wartości:
<java>
return ZWRACANA_WART;
</java>
W metodzie musi znajdować się conajmniej jedna instrukcja return (wyjątkiem jest typ zwracany void, jak w przypadku metody main). Z metody można zwrócić dowolną wartość, pod warunkiem, że jej typ zgadza się z tym określonym w definicji metody.`;

export const instruction = `Nie zmieniając implementacji metody main, zdefiniuj metodę statyczną makeTriple zwracającą typ int, tak aby wywołanie jej pozwalało uzyskać potrojoną wartość przekazanego parametru.`;

export const hasNext = false;