import { useEffect } from 'react';
import styled, { keyframes } from 'styled-components';

const rotate = keyframes`
  to {
    transform: rotate(360deg);
  }
`;

const pulse = keyframes`
  0%, 100% {
    box-shadow: 0 0 10px 2px rgba(165, 180, 252, 0.5);
  }
  50% {
    box-shadow: 0 0 15px 5px rgba(165, 180, 252, 0.8);
  }
`;

const LoaderContainer = styled.div`
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background: rgba(30, 27, 75, 0.6); /* Soft, semi-transparent background */
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 9999;
`;

const Ring = styled.div`
  width: 60px;
  height: 60px;
  border: 4px solid transparent;
  border-top-color: #a5b4fc; /* Pastel blue */
  border-bottom-color: #f0abfc; /* Pastel pink */
  border-radius: 50%;
  animation: ${rotate} 2s linear infinite, ${pulse} 3s ease-in-out infinite;
`;

const SimpleRingLoader = () => {
  useEffect(() => {
    return () => {};
  }, []);

  return (
    <LoaderContainer>
      <Ring />
    </LoaderContainer>
  );
};

export default SimpleRingLoader;