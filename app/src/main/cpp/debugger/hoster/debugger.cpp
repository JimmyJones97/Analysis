#include <cstdio>
#include <cstring>
#include <cerrno>
#include <elf.h>
#include <unistd.h>
#include <cstdlib>
#include <sys/mman.h>
#include <cstdint>
#include <sys/stat.h>
#include <fcntl.h>
#include <linenoise.h>


void completion(const char *buf, linenoiseCompletions *lc) {
    if (buf[0] == 'h') {
        linenoiseAddCompletion(lc, "hello");
        linenoiseAddCompletion(lc, "hello there");
    }
}

char *hints(const char *buf, int *color, int *bold) {
    if (!strcasecmp(buf, "hello")) {
        *color = 35;
        *bold = 0;
        return " World";
    }
    return NULL;
}


int main(int argc, char **argv) {
    char *line;
    char *prgname = argv[0];

    /* Parse options, with --multiline we enable multi line editing. */
    while (argc > 1) {
        argc--;
        argv++;
        if (!strcmp(*argv, "--multiline")) {
            linenoiseSetMultiLine(1);
            printf("Multi-line mode enabled.\n");
        } else if (!strcmp(*argv, "--keycodes")) {
            linenoisePrintKeyCodes();
            exit(0);
        } else {
            fprintf(stderr, "Usage: %s [--multiline] [--keycodes]\n", prgname);
            exit(1);
        }
    }

    /* Set the completion callback. This will be called every time the
     * user uses the <tab> key. */
    linenoiseSetCompletionCallback(completion);
    linenoiseSetHintsCallback(hints);

    int fd, i;
    uint8_t *mem;
    struct stat st;
    char *StringTable, *interp;

    Elf32_Ehdr *ehdr;
    Elf32_Phdr *phdr;
    Elf32_Shdr *shdr;

    if (argc < 2) {
        printf("Usage: %s <executable>\n", argv[0]);
        exit(0);
    }

    if ((fd = open(argv[1], O_RDONLY)) < 0) {
        perror("open");
        exit(EXIT_FAILURE);
    }

    if (fstat(fd, &st) < 0) {
        perror("fstat");
        exit(EXIT_FAILURE);
    }

    mem = (uint8_t *) mmap(nullptr, st.st_size, PROT_READ, MAP_PRIVATE, fd, 0);
    if (mem == MAP_FAILED) {
        perror("mmap");
        exit(EXIT_FAILURE);
    }

    ehdr = (Elf32_Ehdr *) mem;
    phdr = (Elf32_Phdr *) &mem[ehdr->e_phoff];
    shdr = (Elf32_Shdr *) &mem[ehdr->e_shoff];

    if (mem[0] != 0x7f && strcmp(reinterpret_cast<const char *>(&mem[1]), "ELF")) {
        fprintf(stderr, "%s is not an ELF file,%s \n", argv[1], &mem[1]);
        exit(EXIT_FAILURE);
    }

    if (ehdr->e_type != ET_EXEC) {
        fprintf(stderr, "%s is not an executable. \n", argv[1]);
        switch (ehdr->e_type) {
            case ET_CORE:
                printf("type:%x", ehdr->e_type);
                break;
            case ET_DYN:
                printf("type:%x", ehdr->e_type);
                break;
            case ET_EXEC:
                printf("type:%x", ehdr->e_type);
                break;
            case ET_REL:
                printf("type:%x", ehdr->e_type);
                break;
            case ET_NONE:
                printf("type:%x", ehdr->e_type);
                break;
            default:
                printf("type:%x", ehdr->e_type);
                break;
        }
        exit(EXIT_FAILURE);
    }

    printf("Program Entry point: 0x%x\n", ehdr->e_entry);

    StringTable = (char *) &mem[shdr[ehdr->e_shstrndx].sh_offset];

    printf("Section header list:\n\n");
    for (i = 1; i < ehdr->e_shnum; i++)
        printf("%s: 0x%x\n", &StringTable[shdr[i].sh_name], shdr[i].sh_addr);

    printf("\nProgram header list\n\n");
    for (i = 0; i < ehdr->e_phnum; i++) {
        switch (phdr[i].p_type) {
            case PT_LOAD:
                if (phdr[i].p_offset == 0)
                    printf("Text segment: 0x%x\n", phdr[i].p_vaddr);
                else
                    printf("Data segment: 0x%x\n", phdr[i].p_vaddr);
                break;
            case PT_INTERP:
                interp = strdup((char *) &mem[phdr[i].p_offset]);
                printf("Interpreter: %s\n", interp);
                break;
            case PT_NOTE:
                printf("Note segment: 0x%x\n", phdr[i].p_vaddr);
                break;
            case PT_DYNAMIC:
                printf("Dynamic segment: 0x%x\n", phdr[i].p_vaddr);
                break;
            case PT_PHDR:
                printf("Phdr segment: 0x%x\n", phdr[i].p_vaddr);
                break;
        }
    }

    exit(0);
}