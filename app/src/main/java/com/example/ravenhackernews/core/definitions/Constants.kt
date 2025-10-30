
package com.example.ravenhackernews.core.definitions

object Constants {
    const val DEFAULT_QUERY = "android"
    const val CODE_NO_INTERNET = 101
    const val CODE_TIMEOUT = 102
    const val CODE_NETWORK_ERROR = 104
    const val CODE_HTTP_ERROR = 503
    const val CODE_MALFORMED_JSON = 802
    const val COMMON_GENERIC_ERROR_TEXT_ES = "Hay problemas de comunicación con el servidor. Por favor, inténtalo más tarde"
    const val COMMON_NOT_NETWORK_AVAILABLE_ES = "Revisa tu conexión y vuelve a intentarlo."
    const val COMMON_ERROR_PARSER_JSON = "JsonError"

    fun getGenericErrorHtml(message: String = "No se pudo cargar la página. Intenta nuevamente más tarde."): String {
        return """
        <!DOCTYPE html>
        <html lang="es">
        <head>
            <meta charset="UTF-8">
            <meta name="viewport" content="width=device-width, initial-scale=1.0">
            <style>
              
                .container {
                    text-align: center;
                    background: #fff;
                    padding: 24px;
                    border-radius: 12px;
                    box-shadow: 0 4px 10px rgba(0,0,0,0.1);
                    max-width: 320px;
                    width: 85%;
                }
                h2 {
                    color: #d9534f;
                    margin-bottom: 12px;
                    font-size: 20px;
                }
                p {
                    font-size: 15px;
                    color: #555;
                    margin-bottom: 0;
                }
            </style>
        </head>
        <body>
            <div class="container">
                <h2>⚠️ Algo salió mal</h2>
                <p>$message</p>
            </div>
        </body>
        </html>
    """.trimIndent()
    }
}
