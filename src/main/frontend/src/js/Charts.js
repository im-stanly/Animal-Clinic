import React, { useEffect, useState } from 'react';
import { Line } from 'react-chartjs-2';
import randomColor from 'randomcolor';
import 'chart.js/auto';

const VetRatingChart = () => {
  const [visitData, setVisitData] = useState([]);

  useEffect(() => {
    fetch('http://localhost:8080/visits')
      .then(response => response.json())
      .then(data => {
        setVisitData(data);
      })
      .catch(error => {
        console.error('Error fetching visit data:', error);
      });
  }, []);

  const renderChart = () => {
    const uniqueVetIds = getUniqueVetIds();
    const datasets = [];
    let labels = [];

    uniqueVetIds.forEach(vetId => {
      const ratingData = getRatingData(vetId);
      const ratingLabels = [];
      const data = [];

      ratingData.forEach(visit => {
        if (visit.rating !== null) {
          ratingLabels.push(visit.visit_date);
          data.push(visit.rating);
        }
      });

      labels = [...labels, ...ratingLabels];

      const randomColorValue = randomColor(); // Generate a random color for each dataset

      const dataset = {
        label: `Vet ${vetId}`,
        data: data,
        fill: false,
        borderColor: randomColorValue,
      };

      datasets.push(dataset);
    });

    const chartData = {
      labels: labels,
      datasets: datasets,
    };

    const options = {
      scales: {
        y: {
          beginAtZero: true,
          max: 5,
          ticks: {
            stepSize: 0.5,
          },
        },
      },
    };

    return (
      <div className="chart">
        <Line data={chartData} options={options} />
      </div>
    );
  };

  const getUniqueVetIds = () => {
    const uniqueVetIds = Array.from(new Set(visitData.map(visit => visit.vet_id)));
    return uniqueVetIds;
  };

  const getRatingData = vetId => {
    const ratingData = visitData
      .filter(visit => visit.vet_id === vetId)
      .map(visit => ({
        visit_date: visit.visit_date,
        rating: visit.rate,
      }));

    return ratingData;
  };

  return (
    <div className="chart-container">
      {visitData.length > 0 ? renderChart() : <p>Loading data...</p>}
    </div>
  );
};

export default VetRatingChart;
