import React, { useState } from 'react';
import * as XLSX from 'xlsx';
import { Button, Container, Paper, Typography, LinearProgress } from '@mui/material';
import { Upload as UploadIcon } from '@mui/icons-material';

const DismantleSheetValidation = () => {
  const [excelData, setExcelData] = useState(null);
  const [isLoading, setIsLoading] = useState(false);

  const handleFileUpload = (e) => {
    const file = e.target.files[0];
    if (!file) return;

    setIsLoading(true);
    
    const reader = new FileReader();
    reader.onload = (e) => {
      try {
        const data = new Uint8Array(e.target.result);
        const workbook = XLSX.read(data, { type: 'array' });
        const firstSheetName = workbook.SheetNames[0];
        const worksheet = workbook.Sheets[firstSheetName];
        const jsonResult = XLSX.utils.sheet_to_json(worksheet);
        
        setExcelData(jsonResult);
      } catch (err) {
        console.error('Error:', err);
      } finally {
        setIsLoading(false);
      }
    };
    reader.readAsArrayBuffer(file);
  };

  return (
    <Container maxWidth="md" sx={{ mt: 4 }}>
      <Paper elevation={3} sx={{ p: 3, textAlign: 'center' }}>
        <Typography variant="h5" gutterBottom>
          Excel to JSON Converter
        </Typography>
        
        <Button
          component="label"
          variant="contained"
          startIcon={<UploadIcon />}
          sx={{ mb: 2 }}
        >
          Upload Excel File
          <input
            type="file"
            hidden
            accept=".xlsx, .xls"
            onChange={handleFileUpload}
          />
        </Button>

        {isLoading && <LinearProgress sx={{ mb: 2 }} />}

        {excelData && (
          <div>
            <Typography variant="h6" gutterBottom>
              Converted JSON Data
            </Typography>
            <Paper elevation={2} sx={{ p: 2, overflow: 'auto', maxHeight: 400 }}>
              <pre>{JSON.stringify(excelData, null, 2)}</pre>
            </Paper>
          </div>
        )}
      </Paper>
    </Container>
  );
};

export default DismantleSheetValidation;