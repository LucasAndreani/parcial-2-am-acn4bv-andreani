# Informe – Aplicaciones Móviles (Parcial 2)

## General
La aplicación es una agenda personal pensada para organizar bloques de hábitos y tareas de forma simple. El usuario puede ingresar su nombre, moverse entre días, agregar tareas con hora y descripción, editarlas, eliminarlas y realizar búsquedas. Incluye una pantalla de perfil, una guía de ayuda y almacenamiento SQLite.

## Pantallas y Flujos

### MainActivity (Pantalla de ingreso)
Pantalla inicial de la app. Diseño en ConstraintLayout con algunos detalles decorativos. El usuario escribe su nombre y toca “Entrar a mi tablero”. Si el campo está vacío, aparece un mensaje con un Snackbar. Cuando el nombre es válido, se guarda con SharedPreferences y se pasa a la AgendaActivity.

### AgendaActivity (Pantalla principal)
Pantalla central donde se ve la agenda. Tiene un toolbar con acceso al perfil, la ayuda y la búsqueda. El contenido principal se muestra dentro de un fragmento. También hay un botón para volver a la pantalla inicial y limpiar la navegación. Desde esta pantalla se accede a casi todo lo demás.

### TasksListFragment (Listado de tareas)
En este fragmento se muestran las tareas del día seleccionado. Arriba se ve la fecha para cambiar de día. La lista se maneja con un RecyclerView, y se puede agregar o editar un bloque. Cada bloque tiene título, descripción y hora. Todas las operaciones (agregar, editar, borrar) se guardan en SQLite. La búsqueda aplica filtros sobre la lista que ya está cargada. Los mensajes y avisos se muestran con Snackbars.

### ProfileActivity (Perfil)
Es una pantalla de solo lectura que muestra un pequeño resumen del usuario. Se ve un avatar, un saludo con su nombre y algunos datos sacados de SQLite, como la cantidad total de bloques registrados. También usa el nombre guardado en SharedPreferences.

### HelpActivity (Ayuda)
Guía simple para explicar cómo usar la app. Tiene un switch para activar o desactivar el modo oscuro y la elección queda guardada en SharedPreferences. También muestra una lista de tips hecha a partir de un string-array, donde se reemplaza un placeholder con el nombre del usuario.

### Diálogos auxiliares
Esta aplicacion usa dos diálogos principales. Uno es para agregar o editar un bloque, con un formulario dispuesto en un LinearLayout y validaciones para evitar campos vacíos. El otro diálogo es para la búsqueda y permite filtrar resultados según distintos criterios, actualizando directamente lo que se muestra en el fragmento.

## Funcionalidad
La app está dividida en varias pantallas conectadas entre sí y usa tanto ConstraintLayout como LinearLayout según el caso. Los botones y textos están presentes en todas las pantallas para facilitar la interacción. Para guardar información básica del usuario, como su nombre o el modo oscuro, se usan SharedPreferences. Las tareas se manejan con SQLite, lo que permite agregar, editar, borrar y consultar datos de forma local. La lista de bloques del día está implementada con RecyclerView y un Adapter. Los recursos de la app, como colores, textos y temas, están organizados dentro de la carpeta values para mantener todo ordenado.
