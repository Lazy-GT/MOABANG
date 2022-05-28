import React from 'react';
import App from './App';
import { BrowserRouter } from 'react-router-dom';
import { createRoot } from 'react-dom/client';
import { CookiesProvider } from 'react-cookie';
import axios from "axios";

axios.defaults.baseURL = "https://모아방.kr:8080";
axios.defaults.withCredentials = true;

const root = createRoot(document.getElementById('root'));

root.render(
  <BrowserRouter>
    <CookiesProvider>
      <App />
    </CookiesProvider>
  </BrowserRouter>
);
