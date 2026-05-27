module.exports = {
  lintOnSave: false,
  devServer: {
    port: 8080,
    proxy: {
      '/api': {
        target: 'http://localhost:8089',
        changeOrigin: true
      }
    }
  }
}
