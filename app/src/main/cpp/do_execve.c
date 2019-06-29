
#include <errno.h>
#include <stdio.h>
#include <string.h>
#include <unistd.h>

int main(int argc, char *argv[])
{
    char *args[] = {"zero", "one", "two", NULL};
    char *envp[] = {"ENVVAR1=1", "ENVVAR2=2", NULL};
    execve(argv[1], args, envp);
    /* won't reach here if argv[1] can be executed */
    fprintf(stderr, "Failed to execute '%s', %s\n", argv[1], strerror(errno));
    return 1;
}
