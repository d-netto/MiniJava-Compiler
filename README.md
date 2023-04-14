# MiniJava Compiler

Compiler for a subset of Java. 

BNF (Backus-Naur Form) is available at https://www.cambridge.org/resources/052182060X/MCIIJ2e/grammar.htm. 

Test files in `/test/test_files` were taken from https://www.cambridge.org/resources/052182060X/.

## Usage

`Ant 1.10.4+` and `GCC 9.3.0+` are required to build the project, which can be done by running `ant build` in the repo's root. 

To compile and run a `MiniJava` file, run `./minijava.sh <path_to_file>` (also in the repo's root). 

As an example, consider the following implementation of factorial from `factorial.mjava`:

```java
class Factorial {

    public static void main(String[] a) {
        System.out.println(new Fac().ComputeFac(10));
    }
}

class Fac {

    public int ComputeFac(int num) {
        int num_aux;
        if (num < 1)
            num_aux = 1;
        else
            num_aux = num * (this.ComputeFac(num - 1));
        return num_aux;
    }

}
```

Running:

```
ant build
./minijava.sh test/test_files/factorial.mjava
```

outputs 3628800, which is 10!

## Compatibility Note

The current implementation has only been tested on Linux, and will probably not work on macOS.

## Example of AT&T x86 Assembly Code Generation

The recursive implementation of factorial mentioned above compiles down to:

```assembly
.data
stdout_buffer:
	.string "%d\n"
Fac$$:
	.quad 0
	.quad Fac$ComputeFac
	.align 16
.text
.global main

main:
	pushq %rbp
	movq %rsp, %rbp
	movq $1, %rdi
	movq $8, %rsi
	call calloc
	leaq Fac$$, %rdx
	movq %rdx, 0(%rax)
	pushq %rax
	movq 0(%rax), %rax
	movq 8(%rax), %rax
	pushq %rax
	movq $10, %rax
	pushq %rax
	popq %rsi
	popq %rax
	popq %rdi
	call *%rax
	leaq stdout_buffer, %rdi
	movq %rax, %rsi
	call printf
	movq $0, %rax
	movq %rbp, %rsp
	popq %rbp
	ret

Fac$ComputeFac:
	pushq %rbp
	movq %rsp, %rbp
	subq $32, %rsp
	movq %rdi, -8(%rbp)
	movq %rsi, -16(%rbp)
	movq $0, -24(%rbp)
	movq -16(%rbp), %rax
	pushq %rax
	movq $1, %rax
	popq %rdx
	cmpq %rax, %rdx
	movq $0, %rax
	jge block$1
	movq $0xFFFFFFFFFFFFFFFF, %rax
block$1:
	test %rax, %rax
	jz block$2
	leaq -24(%rbp), %rax
	pushq %rax
	movq $1, %rax
	popq %rdx
	movq %rax, 0(%rdx)
	jmp block$3
block$2:
	leaq -24(%rbp), %rax
	pushq %rax
	movq -16(%rbp), %rax
	pushq %rax
	movq -8(%rbp), %rax
	pushq %rax
	movq 0(%rax), %rax
	movq 8(%rax), %rax
	pushq %rax
	movq $1, %rax
	pushq %rax
	movq -16(%rbp), %rax
	popq %rdx
	subq %rdx, %rax
	pushq %rax
	popq %rsi
	popq %rax
	popq %rdi
	call *%rax
	popq %rdx
	mulq %rdx
	popq %rdx
	movq %rax, 0(%rdx)
block$3:
	movq -24(%rbp), %rax
	movq %rbp, %rsp
	popq %rbp
	ret
```
