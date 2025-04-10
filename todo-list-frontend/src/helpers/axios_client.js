const axiosClient = axios.create({
  baseURL: '/',
  headers: {
      'Content-type': 'application/json',
      'X-Requested-With': 'XMLHttpRequest',
      RequestVerificationToken: $('input[name="__RequestVerificationToken"]').val()
  },
  paramsSerializer: params => queryString.stringify(params)
});

axiosClient.interceptors.request.use(function (config) {
  if (config.url) {
      const [baseUrl, queryString] = config.url.split('?');

      if (queryString) {
          const filteredParams = queryString
              .split('&')
              .filter(param => param.split('=')[1]); 

          config.url = filteredParams.length
              ? `${baseUrl}?${filteredParams.join('&')}`
              : baseUrl;
      }
  }

  return config;
}, function (error) {
  return Promise.reject(error);
});

axiosClient.interceptors.response.use(function (response) {
  if (response && !!response.data) {
      return response.data;
  }

  return response;
}, function (error) {
  if (axios.isAxiosError(error)) {
      if (error.response && error.response?.status === 401) {
          // Create overlay container
          const overlay = document.createElement("div");
          overlay.className = "loading-overlay";
          document.body.appendChild(overlay);

          // Render Kendo Loader inside the overlay
          const root = ReactDOM.createRoot(overlay);
          root.render(<Loader size="large" type="converging-spinner" />);

          localStorage.setItem("outSession", "Phiên sử dụng của bạn đã hết, Vui lòng đăng nhập lại hệ thống để tiếp tục sử dụng!");

          setTimeout(() => {
              window.location.href = '/Auth/Login';
          }, 2000);
      }
  }

  return Promise.reject(error);

});

export default axiosClient;
