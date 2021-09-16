import { useEffect } from 'react';

import { useLogout } from '../hooks';

const LogoutPage = () => {
  const { logout } = useLogout();

  useEffect(() => {
    logout({ isRouteMain: true });
  }, []);

  return null;
};

export default LogoutPage;
