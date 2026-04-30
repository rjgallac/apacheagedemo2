/// <reference types='vitest' />
import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';
import { nxViteTsPaths } from '@nx/vite/plugins/nx-tsconfig-paths.plugin';
import { nxCopyAssetsPlugin } from '@nx/vite/plugins/nx-copy-assets.plugin';

export default defineConfig(() => ({
  root: import.meta.dirname,
  cacheDir: '../node_modules/.vite/frontend',
  server: {
    port: 4200,
    host: 'localhost',
    proxy: {
      // Proxy API requests to the backend Spring Boot server
      '/api/person/graph': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        secure: false,
      },
      '/api/person/graph/db': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        secure: false,
      },
      '/api/person/create-node': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        secure: false,
      },
      '/api/person/create-edge': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        secure: false,
      },
      '/api/person': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        secure: false,
      },
      '/api/person/create-node-and-relation': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        secure: false,
      },
      '/api/person/delete-all': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        secure: false,
      },
      '/api/company': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        secure: false,
      },
    },
  },
  preview: {
    port: 4200,
    host: 'localhost',
  },
  plugins: [react(), nxViteTsPaths(), nxCopyAssetsPlugin(['*.md'])],
  // Uncomment this if you are using workers.
  // worker: {
  //   plugins: () => [ nxViteTsPaths() ],
  // },
  build: {
    outDir: '../dist/frontend',
    emptyOutDir: true,
    reportCompressedSize: true,
    commonjsOptions: {
      transformMixedEsModules: true,
    },
  },
  test: {
    name: 'frontend',
    watch: false,
    globals: true,
    environment: 'jsdom',
    include: ['{src,tests}/**/*.{test,spec}.{js,mjs,cjs,ts,mts,cts,jsx,tsx}'],
    reporters: ['default'],
    coverage: {
      reportsDirectory: '../coverage/frontend',
      provider: 'v8' as const,
    },
  },
}));
